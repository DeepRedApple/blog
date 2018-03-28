package com.zzuli.blog.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzuli.blog.domain.es.EsBlog;
import com.zzuli.blog.repository.es.EsBlogRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsBlogRepositoryTests {

	@Autowired
	private EsBlogRepository esBlogRepository;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void testPageable0() {
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		// 1 2 3   3

		Pageable pageable = PageRequest.of(1, 100, sort);
		Page<EsBlog> blogPage = esBlogRepository.findAll(pageable);
		System.out.println(blogPage.getSize());
		Iterator<EsBlog> iterator = blogPage.getContent().iterator();
		System.out.println("===" + blogPage.getTotalElements());
		System.out.println("+++" + blogPage.getTotalPages());
		System.out.println("---" + blogPage.getSize());
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getBlogId());
		}

		/**
		 *  List getContent(); // 返回分页后的数据的列表
		 	int getTotalPages(); // 总页数
		 	long getTotalElements(); // 总数据量
		 	boolean isFirst(); // 是否是第一个数据；
		 	boolean isLast(); // 是否是最后一个数据；
		 	int getNumber(); // 当前页面索引
		 */
		System.out.println("===================================================");
		com.zzuli.blog.util.Page<EsBlog> pageUtil = new com.zzuli.blog.util.Page<EsBlog>(2, 2,
				(int) blogPage.getTotalElements(), blogPage.getContent());
		System.out.println("===" + pageUtil.getTotalCount()); //总数
		System.out.println("+++" + pageUtil.getTotalPage());  //页数总数
		System.out.println("---" + pageUtil.getPageSize());   //页面大小
		System.out.println("___" + pageUtil.getPageNo());     //页码
		List<EsBlog> blogList = pageUtil.getList();
		Iterator<EsBlog> blogIterator = blogList.iterator();
		while (blogIterator.hasNext()) {
			System.out.println(blogIterator.next().getBlogId() + "=======");
		}

		for (int i = 0; i < blogList.size(); i++) {
			EsBlog esBlog = blogList.get(i);
			System.out.println(esBlog.getBlogId());
		}
	}

	@Test
	public void test1() {
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		// 1 2 3   3

		Pageable pageable = PageRequest.of(1, 100, sort);
		Page<EsBlog> blogs = esBlogRepository.findAll(pageable);
		System.out.println(blogs.getSize());
		Iterator<EsBlog> iterator = blogs.getContent().iterator();
		System.out.println("===" + blogs.getTotalElements());
		System.out.println("+++" + blogs.getTotalPages());
		System.out.println("---" + blogs.getSize());
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getBlogId());
		}

		com.zzuli.blog.util.Page<EsBlog> pageUtil = new com.zzuli.blog.util.Page<>(2, 2,
				(int) blogs.getTotalElements(), blogs.getContent());
		System.out.println("===" + pageUtil.getTotalCount());
		System.out.println("+++" + pageUtil.getTotalPage());
		System.out.println("---" + pageUtil.getPageSize());
		System.out.println("___" + pageUtil.getPageNo());
		List<EsBlog> blogList = pageUtil.getList();
		Iterator<EsBlog> blogIterator = blogList.iterator();
		while (blogIterator.hasNext()) {
			System.out.println(blogIterator.next().getBlogId() + "=======");
		}

		for (int i = 0; i < blogList.size(); i++) {
			EsBlog esBlog = blogList.get(i);
			System.out.println(esBlog.getBlogId());
		}


	}

	@Test
	public void test2() {
		SearchResponse blog = elasticsearchTemplate.getClient().prepareSearch("blog").setScroll(new TimeValue(60000))
				.setSize(1000).execute().actionGet();
		while (blog.getHits().getHits().length != 0) {

		}
	}

	@Test
	public void test3() {
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(0, 2, sort);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withIndices("blog")
				.withTypes("blog").withPageable(pageable);

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		SearchQuery searchQuery = nativeSearchQueryBuilder.withQuery(boolQueryBuilder).build();
		Page<EsBlog> page = elasticsearchTemplate.queryForPage(searchQuery, EsBlog.class);
		List<EsBlog> content = page.getContent();
		System.out.println("TotalPages：" + page.getTotalPages());
		System.out.println("Size: " + page.getSize());
		System.out.println("Number: " + page.getNumber());
		System.out.println("TotalElements: " + page.getTotalElements());

		for (EsBlog esBlog : content) {
			System.out.println(esBlog.getBlogId() + " ==== " + esBlog.getTitle());
		}
	}

	@Test
	public void test4() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery("", "title",
				"summary", "content", "tags")).withPageable(PageRequest.of(0, 6, new Sort(Sort.Direction.DESC,
				"createTime"))).build();
		Page<EsBlog> page = elasticsearchTemplate.queryForPage(searchQuery, EsBlog.class);
		List<EsBlog> content = page.getContent();
		for (EsBlog blog : content) {
			System.out.println(blog.getBlogId() + " === " + blog.getSummary() + " +++ " + blog.getTitle() + " *** "
					+ blog.getTags());
		}
	}

	@Test
	public void test5() {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withIndices("blog")
				.withTypes("blog").withPageable(PageRequest.of(2, 2, new Sort(Sort.Direction.DESC, "createTime")));

		String keyword = "Hibernate";
		if (StringUtils.isNotEmpty(keyword)) {
			QueryBuilder queryBuilder = multiMatchQuery(keyword, "title", "summary", "content", "tags");
			nativeSearchQueryBuilder.withQuery(queryBuilder);
		}
		SearchQuery searchQuery = nativeSearchQueryBuilder
//				.withHighlightFields(
//						new HighlightBuilder.Field("title").preTags("<span style='color:red'>").postTags("</span>"),
//						new HighlightBuilder.Field("summary").preTags("<span style='color:red'>").postTags("</span>"),
//						new HighlightBuilder.Field("content").preTags("<span style='color:red'>").postTags("</span>"),
//						new HighlightBuilder.Field("tags").preTags("<span style='color:red'>").postTags("</span>")
//						)
				.build();
		Page<EsBlog> page = elasticsearchTemplate.queryForPage(searchQuery, EsBlog.class);
		System.out.println("size: " + page.getSize());
		System.out.println("TotalPages: " + page.getTotalPages());
		System.out.println("TotalElements: " + page.getTotalElements());
		System.out.println("NumberOfElements: " + page.getNumberOfElements());
		System.out.println("Number: " + page.getNumber());
		Iterator<EsBlog> iterator = page.getContent().iterator();
		while (iterator.hasNext()) {
			EsBlog esBlog = iterator.next();
			System.out.println(esBlog.getTitle() + " == " + esBlog.getSummary() + " ++ "+esBlog.getTags());
		}
	}

	@Test
	public void test6() throws IOException {
		String keyword = "数据持久化";
		SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch("blog");
		searchRequestBuilder.setTypes("blog");
		//设置查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCAN 扫描查询无序
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		//设置要查询的关键词
		searchRequestBuilder.setQuery(multiMatchQuery(keyword, "title", "summary", "content", "tags"));
		//分页
		searchRequestBuilder.setFrom(2).setSize(2); //searchRequestBuilder.setFrom((pageNo - 1) * pageSize).setSize(pageSize)
		searchRequestBuilder.addSort("createTime", SortOrder.DESC);
		//是否按照匹配度排序
		searchRequestBuilder.setExplain(true);
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").field("summary")
				.field("tags").field("content").requireFieldMatch(false);
		highlightBuilder.preTags("<span style='color:red'>");
		highlightBuilder.postTags("</span>");
		searchRequestBuilder.highlighter(highlightBuilder);
		//设置高亮
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = searchResponse.getHits();
		SearchHit[] hits = searchHits.getHits();
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String json = hit.getSourceAsString();
			EsBlog esBlog = mapper.readValue(json, EsBlog.class);
			Map<String, HighlightField> result = hit.getHighlightFields();
			HighlightField title = result.get("title");
			HighlightField summary = result.get("summary");
			HighlightField content = result.get("content");
			HighlightField tags = result.get("tags");

			if (null != tags) {
				Text[] textsTags = tags.getFragments();
				String tagsStr = "";
				for (Text text : textsTags) {
					tagsStr += text;
				}
				esBlog.setTags(tagsStr);
			}

			if (null != title) {
				Text[] textsTitle = title.getFragments();
				String titleStr = "";
				for (Text text : textsTitle) {
					titleStr += text;
				}
				esBlog.setTitle(titleStr);
			}

			if (null  != summary) {
				Text[] textSummary = summary.getFragments();
				String summaryStr = "";
				for (Text text : textSummary) {
					summaryStr += text;
				}
				esBlog.setSummary(summaryStr);
			}

			if (null != content) {
				Text[] textContent = content.getFragments();
				String contentStr = "";
				for (Text text : textContent) {
					contentStr += text;
				}
				esBlog.setContent(contentStr);
			}

			System.out.println(esBlog.getTitle());
			System.out.println(esBlog.getTags());
			System.out.println(esBlog.getSummary());
			System.out.println(esBlog.getContent());
		}

	}



}
