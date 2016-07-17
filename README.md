<center><h1>基于Lucene的信息搜索引擎</h1></center>
<center><h2>现代信息检索工程类搜索型大作业   2016年5月</h2></center>
#一、选题
工程类搜索型: 
	
> 定向采集 3-4 个新闻网站, 实现这些网站信息的抽取、索引和检索。网页数 目不少于 10 万条。能按相关度、时间、热度 (需要自己定义) 等属性进行排序, 能实现相似 新闻的自动聚类。
	要求: 有相关搜索推荐、snippet 生成、结果预览 (鼠标移到相关结果, 能预览) 功能

#二、项目线上访问地址
-	项目访问地址:http://120.27.46.201:8080/UCASIR/ （检索体育相关新闻）
-	代码托管在 github 上:https://github.com/cseryp/UCASIR
# 三、开发工具
-	jdk 版本:1.7 
-	服务器:Tomcat7.0 
-	python:2.7 
-	scrapy:1.0
# 四、设计方案
##4.1整体思路
在实现新闻信息检索系统时首先进行了信息采集,信息采集结束之后使用 Lucene 提供的 api 构建索引库, 前端使用 jsp 接收用户查询,在后台使用 servlet 对用户查询进 行分词处理,之后到索引库中进行文档匹配, 最后把查询结果集反馈给用户并在前端页面中 展示。
##4.2信息采集
Scrapy 是 Python 开发的一个快速、高层次的屏幕抓取和 web 抓取框架,用于抓取 web 站点并从页面中提取结构化的数据。我们采集了 10w+ 新闻数据, 新闻信息以 json 文件格 式保存。scrapy 采集流程：
![这里写图片描述](http://img.blog.csdn.net/20160523095551005)
采集到的新闻数据：
![这里写图片描述](http://img.blog.csdn.net/20160522223441662)
新闻格式：
![这里写图片描述](http://img.blog.csdn.net/20160522223522322)

##4.3倒排索引构建
在索引构建模块,主要包括下面三个关键步骤: 数据预处理、新闻内容分词、构建倒排 索引。
**数据预处理:**Gson 是 Google 提供的用来在 Java 对象和 JSON 数据之间进行映射的 Java 类库。可以将一个 JSON 字符串转成一个 Java 对象, 或者反过来。我们使用 Gson 把 json 文件转换为 News 对象
**中文分词:**IK Analyzer 是一个开源的,基于 java 语言开发的轻量级的中文分词工具 包,IK 发展为面向 Java 的公用分词组件,独立于 Lucene 项目,同时提供了对 Lucene 的默 认优化实现。IK 分词采用了特有的“正向迭代最细粒度切分算法“, 支持细粒度和智能分词两 种切分模式, 并且采用了多子处理器分析模式, 支持英文字母、数字、中文词汇等分词处理, 兼容韩文、日文字符。
**构建倒排索引:**Lucene 提供了构建倒排索引的方法,步骤如下图所示:
![这里写图片描述](http://img.blog.csdn.net/20160523095607940)
Luke 是一个用于 Lucene 搜索引擎的,方便开发和诊断的第三方工具,它可以访问
现有 Lucene 的索引. 使用 luke 打开索引目录,可以看到新闻信息被存储到索引库中。
![这里写图片描述](http://img.blog.csdn.net/20160522223720840)
##4.4索引查询
索引创建好以后,查询可分为以下几个步骤:
1.  设置查询索引的目录 (这里就是上面创建索引的目录).
2. 创建 indexSearcher.
3. 设置 query 的分词方式
4. 设置查询域, 比如查询域为 news title, 那么就到新闻标题域去比对 5. 设置查询字符串,也就是要查询的关键词.
6. 返回结果是一个文档集合,放在 TopDocs 中,通过循环 TopDocs 数组输出查询结果. 用户一般只看前几页的数据,为了加快前端的数据展示, 返回前 1000 条数据到前台。

##4.5关键字高亮
检索结果高亮对于用户的体验度和友好度非常重要,可以快速标记出用户检索对关键 词。Lucene 的 Highlighter 类可以用于返回文档中的关键字高亮,通过在关键字前面添加 css 片段来实现。 
#4.6用户界面
使用 jsp 编写用户界面,服务器为 Tomcat 7.0, 用户输入关键词以后提交表单,后
台使用 servlet 接收用户查询, 之后把查询字符串作为搜索的 key 到索引库中搜索文档。检索效果：
![这里写图片描述](http://img.blog.csdn.net/20160522223958335)
#4.7按时间排序和结果分页

**按时间排序:** 所有的新闻结果存放在一个 list 集合中,集合中的每个元素为一个 News 对象, 通过重写 Comparator 类中的 compare 方法实现按集合中每个新闻元素的 Time 排 序。
**结果分页:** 定义了一个 Page 类用来记录当前页、总页数、每页多少条数据、总的数据 数、每页起始数、每页终止数、是否有下一页、是否有前一页。


# 五、参考资料
下面列出可参考的资料：
1.	[lucene全文检索基础](http://blog.csdn.net/napoay/article/details/50305287)
2.	[lucene创建索引](http://blog.csdn.net/napoay/article/details/50305413)
3. [Lucene查询索引](http://blog.csdn.net/napoay/article/details/50305413)
4. [Lucene查询结果高亮](http://blog.csdn.net/napoay/article/details/51190428)
5. [Lucene 查询(Query)子类](http://blog.csdn.net/napoay/article/details/51227794)
6. [java操作json](http://blog.csdn.net/napoay/article/details/50960137)
7. [java集合](http://blog.csdn.net/napoay/article/details/50930323)
8. [servlet基础](http://blog.csdn.net/napoay/article/details/50770642)

#六、总结
开源工具的使用显著提高了开 发效率,但build from ground还需要学习很多东西，继续沉淀。
欢迎批评指正。


