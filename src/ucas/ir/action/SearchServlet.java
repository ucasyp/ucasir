package ucas.ir.action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ucas.ir.pojo.News;
import ucas.ir.pojo.Page;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int totalnews = 0;
	private static final int perpagecount = 5;

	public SearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 表单参数处理
		String query = request.getParameter("query");
		query = new String(query.getBytes("iso8859-1"), "UTF-8");

		// 计算查询时间
		long starTime = System.currentTimeMillis();// start time
		String indexpathStr = request.getServletContext().getRealPath("/index");
		if (query != null && " ".equals(query) != true) {

			String pagenum = request.getParameter("p");
			System.out.println("pagenum:" + pagenum);
			int p = pagenum == null ? 1 : Integer.parseInt(pagenum);
			System.out.println("p:" + p);

			// 获取排序方式
			String sortmethod = request.getParameter("sortnews");
			System.out.println("排序方式:" + sortmethod);
			ArrayList<News> newsList = getTopDoc(query, indexpathStr);

			Page page = new Page(p, newsList.size() / perpagecount + 1, perpagecount, newsList.size(),
					perpagecount * (p - 1), perpagecount * p, true,p==1?false:true);
			System.out.println(page.toString());
			if ("byTime".equals(sortmethod)) {

				Collections.sort(newsList, new SortByTime());
			}

			List<News> pagelist = newsList.subList(perpagecount * (p - 1), perpagecount * p);
			// 设置分页

			System.out.println("servlet newslist length:" + newsList.size());
			request.setAttribute("query", query);
			request.setAttribute("newslist", pagelist);
			request.setAttribute("queryback", query);
			request.setAttribute("totaln", totalnews);
			long endTime = System.currentTimeMillis();// end time
			long Time = endTime - starTime;
			request.setAttribute("time", (double) Time / 1000);
			request.setAttribute("page", page);
			request.getRequestDispatcher("result.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}

	}

	public static ArrayList<News> getTopDoc(String key, String indexpathStr) {
		ArrayList<News> newsList = new ArrayList<News>();

		Directory directory = null;
		try {
			File indexpath = new File(indexpathStr);
			if (indexpath.exists() != true) {
				indexpath.mkdirs();
			}
			// 设置要查询的索引目录
			directory = FSDirectory.open(indexpath);
			// 创建indexSearcher
			DirectoryReader dReader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(dReader);

			String[] fields = { "news_title", "news_article" };
			// 设置分词方式
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);// 标准分词
			MultiFieldQueryParser parser2 = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer);
			Query query2 = parser2.parse(key);

			QueryScorer scorer = new QueryScorer(query2, fields[0]);
			SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");
			Highlighter highlighter = new Highlighter(fors, scorer);
			// 返回前10条
			TopDocs topDocs = searcher.search(query2, 500);
			if (topDocs != null) {
				totalnews = topDocs.totalHits;
				System.out.println("符合条件第文档总数：" + totalnews);
				for (int i = 0; i < topDocs.scoreDocs.length; i++) {
					Document doc = searcher.doc(topDocs.scoreDocs[i].doc);

					TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(),
							topDocs.scoreDocs[i].doc, fields[0], analyzer);
					Fragmenter fragment = new SimpleSpanFragmenter(scorer);
					highlighter.setTextFragmenter(fragment);

					String hl_title = highlighter.getBestFragment(tokenStream, doc.get("news_title"));

					tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), topDocs.scoreDocs[i].doc,
							fields[1], analyzer);
					String hl_summary = highlighter.getBestFragment(tokenStream, doc.get("news_article"));

					News news = new News(doc.get("news_id"), hl_title != null ? hl_title : doc.get("news_title"),
							doc.get("news_keywords"), doc.get("news_posttime"), doc.get("news_source"),
							hl_summary != null ? hl_summary : doc.get("news_article"), doc.get("news_total"),
							doc.get("news_url"), doc.get("news_reply"), doc.get("news_show"));
					newsList.add(news);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return newsList;
	}

}
