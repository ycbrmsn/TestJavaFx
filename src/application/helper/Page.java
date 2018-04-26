package application.helper;

public class Page {
  
  private int pageIndex;
  private int pageSize;
  private int total;
  private int pages;

  public Page() {
    
  }
  
  public Page(int pageSize, int total) {
    this.pageSize = pageSize;
    this.total = total;
    if (total == 0) {
      this.pages = 1;
    } else {
      this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }
  }
  
  public Page(int pageIndex, int pageSize, int total) {
    this(pageSize, total);
    if (pageIndex >= this.pages) {
      pageIndex = this.pages - 1;
    }
    if (pageIndex < 0) {
      pageIndex = 0;
    }
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  @Override
  public String toString() {
    return "Page [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", total=" + total + ", pages=" + pages + "]";
  }
  
}
