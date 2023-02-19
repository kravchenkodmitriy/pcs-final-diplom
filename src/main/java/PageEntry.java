import java.io.File;
import java.util.Map;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(File pdfName, int page, Map.Entry<String, Integer> entry) {

        this.pdfName = pdfName.getName();
        this.page = page;
        this.count = entry.getValue();
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
    @Override
    public int compareTo(PageEntry o) {

        return Integer.compare(count, o.count);
    }

    @Override
    public String toString(){
//        return String.valueOf(new PageEntry(getPdfName(), getPage(), getCount()));

        return "\n {" + " \n" +
                "   \"pdfName\": \"" + pdfName + "\", \n" +
                "   \"page\": " + page + ", \n" +
                "   \"count\": " + count + " \n" +
                " }";
    }
}
