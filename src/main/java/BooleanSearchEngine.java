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
    private List<PageEntry> resultPage = new ArrayList<>();


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

                for (Map.Entry<String, Integer> entry : freqs.entrySet()){
                    List<PageEntry> pageEntryList = new ArrayList<>();
                    String key = entry.getKey();
                    PageEntry pageEntry = new PageEntry(file, pages, entry);
                    pageEntryList.add(pageEntry);
                    wordList.put(key, pageEntryList);


                }
//                for (String word : freqs.keySet()){
//                    PageEntry pageEntry = new PageEntry(file.getName(), pages, freqs.get(word));
//                    if (!wordList.containsKey(word)) {
//                        List<PageEntry> pageList = new ArrayList<>();
//                        pageList.add(pageEntry);
//                        wordList.put(word, pageList);
//                    } else {
//                        wordList.get(word).add(pageEntry);
//                    }
//
//                    resultPage = new ArrayList<>(wordList.get(word.toLowerCase()));
//                    resultPage.sort(Collections.reverseOrder());
//                }

            }
        }
        //получить объект одной страницы и полистать
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntryList = this.wordList.getOrDefault(word, Collections.emptyList());
        Collections.sort(pageEntryList);
        return pageEntryList;
    }
}
