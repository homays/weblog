package com.arrebol.weblog.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class LuceneHelper {

    /**
     * 创建索引
     * @param indexDir 索引存放的目录
     * @param documents 文档
     */
    public void createIndex(String indexDir, List<Document> documents) {
        try {
            File dir = new File(indexDir);

            // 判断索引目录是否存在
            if (dir.exists()) {
                // 删除目录中的内容
                FileUtils.cleanDirectory(dir);
            } else {
                // 若不存在，则创建目录
                FileUtils.forceMkdir(dir);
            }

            // 读取索引目录
            Directory directory = FSDirectory.open(Paths.get(indexDir));

            // 中文分析器
            Analyzer analyzer = new SmartChineseAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            // 创建索引
            IndexWriter writer = new IndexWriter(directory, config);

            // 添加文档
            documents.forEach(document -> {
                try {
                    writer.addDocument(document);
                } catch (IOException e) {
                    log.error("添加 Lucene 文档错误: ", e);
                }
            });

            // 提交
            writer.commit();
            writer.close();
        } catch (Exception e) {
            log.error("创建 Lucene 索引失败: ", e);
        }
    }

    /**
     * 关键词搜索, 查询总数据量
     * @param indexDir 索引目录
     * @param word 查询关键词
     * @param columns 需要搜索的字段
     */
    public long searchTotal(String indexDir, String word, String[] columns) {
        try {
            // 打开索引目录
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // 中文分析器
            Analyzer analyzer = new SmartChineseAnalyzer();
            // 查询解析器
            QueryParser parser = new MultiFieldQueryParser(columns, analyzer);
            // 解析查询关键字
            Query query = parser.parse(word);

            // 搜索文档
            TopDocs totalDocs  = searcher.search(query, Integer.MAX_VALUE);
            // 返回文档数
            return totalDocs.totalHits.value;
        } catch (Exception e) {
            log.error("查询 Lucene 错误: ", e);
            return 0;
        }
    }

    /**
     * 关键词搜索
     * @param indexDir 索引目录
     * @param word 查询关键词
     * @param columns 被搜索的字段
     * @param current 当前页
     * @param size 每页数据量
     * @return
     */
    public List<Document> search(String indexDir, String word, String[] columns, int current, int size) {
        try {
            // 打开索引目录
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // 中文分析器
            Analyzer analyzer = new SmartChineseAnalyzer();
            // 查询解析器
            QueryParser parser = new MultiFieldQueryParser(columns, analyzer);
            // 解析查询关键字
            Query query = parser.parse(word);

            // 执行搜索，获取匹配查询的前 limit 条结果。
            int limit = current * size;
            TopDocs topDocs = searcher.search(query, limit); // 搜索前 limit 条结果

            // 匹配的文档数组
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            // 计算分页的起始 - 结束位置
            int start = (current - 1) * size;
            int end = Math.min(start + size, scoreDocs.length);

            // 返回指定页码的文档
            List<Document> documents = new ArrayList<>();
            for (int i = start; i < end; i++) {
                Document doc = searcher.doc(scoreDocs[i].doc);
                documents.add(doc);
            }

            // 释放资源
            reader.close();
            return documents;
        } catch (Exception e) {
            log.error("查询 Lucene 错误: ", e);
            return null;
        }
    }
}