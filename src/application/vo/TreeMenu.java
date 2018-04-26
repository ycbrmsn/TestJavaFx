package application.vo;

import java.util.List;

public class TreeMenu {

  private Integer id;
  private Integer pid;
  private String title;
  private String icon;
  private Integer level;
  private Integer orderBy;
  private String target;
  private String frame;
  private String enabled;
  private List<TreeMenu> children;
  
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public Integer getPid() {
    return pid;
  }
  public void setPid(Integer pid) {
    this.pid = pid;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }
  public Integer getLevel() {
    return level;
  }
  public void setLevel(Integer level) {
    this.level = level;
  }
  public Integer getOrderBy() {
    return orderBy;
  }
  public void setOrderBy(Integer orderBy) {
    this.orderBy = orderBy;
  }
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }
  public String getFrame() {
    return frame;
  }
  public void setFrame(String frame) {
    this.frame = frame;
  }
  public String getEnabled() {
    return enabled;
  }
  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }
  public List<TreeMenu> getChildren() {
    return children;
  }
  public void setChildren(List<TreeMenu> children) {
    this.children = children;
  }
  
  @Override
  public String toString() {
    return "TreeMenu [id=" + id + ", pid=" + pid + ", title=" + title + ", icon=" + icon + ", level=" + level
        + ", orderBy=" + orderBy + ", target=" + target + ", frame=" + frame + ", enabled=" + enabled + ", children="
        + children + "]";
  }
  
}
