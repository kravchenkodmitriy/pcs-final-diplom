import java.io.File;

import com.google.gson.Gson;

public class PageEntry implements Comparable<PageEntry> {
    private static final Gson gson = new Gson();

    private final String pdfName;
    private final int page;
    private int count;

    public PageEntry(File pdfName, int page) {
        this.pdfName = pdfName.getName();
        this.page = page;
        this.count = 0;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PageEntry)) {
            return false;
        }

        PageEntry p = (PageEntry) o;

        if(!this.pdfName.equals(p.pdfName)) {
            return false;
        }

        if(this.page != p.page) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(count, o.count);
    }

    @Override
    public String toString() {
        // Использую статический объект Gson для сериализации в строку.
        return gson.toJson(this);
    }
}
