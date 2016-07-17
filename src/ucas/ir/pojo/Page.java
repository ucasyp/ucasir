package ucas.ir.pojo;

public class Page {
	private int page = 1;// 当前页
	private int totalPage = 0;// 总页数
	private int pageRecorders;// 每页多少条数据
	private int totalRows = 0;// 总的数据数
	private int startRow = 0;// 每页起始数
	private int endRow = 0;// 每页终止数
	private boolean hasNextPage = false;// 是否有下一页
	private boolean hasPreviousPage = false;// 是否有前一页

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageRecorders() {
		return pageRecorders;
	}

	public void setPageRecorders(int pageRecorders) {
		this.pageRecorders = pageRecorders;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public Page(int page, int totalPage, int pageRecorders, int totalRows, int startRow, int endRow,
			boolean hasNextPage, boolean hasPreviousPage) {
		super();
		this.page = page;
		this.totalPage = totalPage;
		this.pageRecorders = pageRecorders;
		this.totalRows = totalRows;
		this.startRow = startRow;
		this.endRow = endRow;
		this.hasNextPage = hasNextPage;
		this.hasPreviousPage = hasPreviousPage;
	}

	public Page() {
		super();
	}

	@Override
	public String toString() {
		return "Page [page=" + page + ", totalPage=" + totalPage + ", pageRecorders=" + pageRecorders + ", totalRows="
				+ totalRows + ", startRow=" + startRow + ", endRow=" + endRow + ", hasNextPage=" + hasNextPage
				+ ", hasPreviousPage=" + hasPreviousPage + "]";
	}

}
