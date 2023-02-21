import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordList = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        //Создание объекта пдф документа
        for (File file : pdfsDir.listFiles()){
        PdfDocument pdf = new PdfDocument(new PdfReader(file));
            // получить текст со страницы
            for (int i = 1; i < pdf.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(pdf.getPage(i));
                int pages = i;
                //разобрать текст на слова
                var words = text.split("\\P{IsAlphabetic}+");
                //заноести в мапу ключ - слово и значение - список
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.equals(null)){
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                //получить объект одной страницы и полистать
                for (Map.Entry<String, Integer> entry : freqs.entrySet()){
                    List<PageEntry> pageEntryList = new ArrayList<>();
                    String key = entry.getKey();
                    if (wordList.get(key) != null){
                        pageEntryList = wordList.get(key);
                    }
                    PageEntry pageEntry = new PageEntry(file, pages, entry);

                    pageEntryList.add(pageEntry);
                    wordList.put(key, pageEntryList);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntryList = this.wordList.getOrDefault(word, Collections.emptyList());
        Collections.sort(pageEntryList);
        return pageEntryList;
    }
}
