package com.jzb.base.message;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PageInfo<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	// 总记录数
	private long total;
	
	// 总页数
	private int pages;
	
	// 结果集
	private List<T> list;

	/**
	 * 构造对象
	 */
	public PageInfo() {
	} // End PageInfo

	/**
	 * 包装Page对象
	 * @param list page结果
	 */
	public PageInfo(List<T> list) {
		if (list instanceof Page) {
			Page page = (Page) list;
			this.total = page.getTotal();
			this.pages = page.getPages();
			this.list = page;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PageInfo{");
		sb.append("total=").append(total);
		sb.append(", pages=").append(pages);
		sb.append(", list=").append(list);
		sb.append('}');
		return sb.toString();
	}
}
