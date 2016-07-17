package ucas.ir.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ucas.ir.pojo.News;

import javax.servlet.*;

public class CreateIndex {

	public static void main(String[] args) {
		// 第一步：创建分词器
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		// 第二步：创建indexWriter配置信息
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		// 第三步：设置索引的打开方式
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		// 第四步：设置索引第路径
		Directory directory = null;
		// 第五步:创建indexWriter,用于索引第增删改.
		IndexWriter indexWriter = null;

		try {
			File indexpath = new File("./WebContent/index");
			if (indexpath.exists() != true) {
				indexpath.mkdirs();
			}
			directory = FSDirectory.open(indexpath);
			if (indexWriter.isLocked(directory)) {
				indexWriter.unlock(directory);
			}
			indexWriter = new IndexWriter(directory, indexWriterConfig);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 循环创建索引

		ArrayList<String> filenamelist = getfileName();
		Iterator<String> iter = filenamelist.iterator();

		while (iter.hasNext()) {
			// System.out.println(iter.next());
			News news = getNews("/Volumes/My Passport/IR/sportnews/" + iter.next());
			Document doc = new Document();
			if(news!=null){
			System.out.println(news.getTitle());

			doc.add(new TextField("news_id", news.getId(), Store.YES));
			doc.add(new TextField("news_title", news.getTitle(), Store.YES));
			doc.add(new TextField("news_keywords", news.getKeyword(), Store.YES));
			doc.add(new TextField("news_posttime", news.getTime(), Store.YES));
			doc.add(new TextField("news_source", news.getSource(), Store.YES));
			doc.add(new TextField("news_article", news.getArtical(), Store.YES));
			doc.add(new TextField("news_total", news.getTotal(), Store.YES));
			doc.add(new TextField("news_url", news.getURL(), Store.YES));
			doc.add(new TextField("news_reply", news.getReply(), Store.YES));
			doc.add(new TextField("news_show", news.getShow(), Store.YES));
			}
			try {
				indexWriter.addDocument(doc);
				indexWriter.commit();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			indexWriter.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("index create success!");

	}

	// 获取news目录下所有json文件的文件名,返回文件名数组
	public static ArrayList<String> getfileName() {
		ArrayList<String> arrlist = new ArrayList<String>();
		File dataPth = new File("/Volumes/My Passport/IR/sportnews");
		if (dataPth.exists()) {

			File[] allFiles = dataPth.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				arrlist.add(allFiles[i].getName().toString());
			}
		}

		System.out.println(arrlist.size());
		return arrlist;
	}

	// 把json文件解析为News对象,返回值为News对象

	public static News getNews(String path) {
		News news = new News();
		try {
			JsonParser jParser = new JsonParser();
			JsonObject jObject = (JsonObject) jParser.parse(new FileReader(path));
			String id = jObject.get("ID").getAsString();
			String title = jObject.get("Title").getAsString().trim();
			String keyword = jObject.get("Keyword").getAsString();
			String time = jObject.get("Time").getAsString().trim();
			String source = jObject.get("Source").getAsString();
			String artical = jObject.get("Artical").getAsString();
			String total = jObject.get("Total").getAsString();
			String uRL = jObject.get("URL").getAsString();
			String reply = jObject.get("Reply").getAsString();
			String show = jObject.get("Show").getAsString();

			news = new News(id, title, keyword, time, source, artical, total, uRL, reply, show);
			return news;
		} catch (Exception e) {
			return null;
		} 

		

	}
}
