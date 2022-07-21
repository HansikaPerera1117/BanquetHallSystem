package model;

public class MenuSelection {
    private int Num;
    private String MenuId;
    private int MenuTypeNum;
    private String Options;

    public MenuSelection() {
    }

    public MenuSelection(int num, String menuId, int menuTypeNum, String options) {
        Num = num;
        MenuId = menuId;
        MenuTypeNum = menuTypeNum;
        Options = options;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public int getMenuTypeNum() {
        return MenuTypeNum;
    }

    public void setMenuTypeNum(int menuTypeNum) {
        MenuTypeNum = menuTypeNum;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    @Override
    public String toString() {
        return "MenuSelection{" +
                "Num=" + Num +
                ", MenuId='" + MenuId + '\'' +
                ", MenuTypeNum=" + MenuTypeNum +
                ", Options='" + Options + '\'' +
                '}';
    }
}
