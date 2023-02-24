import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordsList = new HashMap<>();

    public BooleanSearchEngine(File dir) throws IOException {
        // Для каждого документа
        for (File file : dir.listFiles()) {
            // Создаю ридер
            var pdf = new PdfDocument(new PdfReader(file));

            // Обхожу каждую страницу текущего документа
            for (var page = 1; page <= pdf.getNumberOfPages(); page++) {
                var text = PdfTextExtractor.getTextFromPage(pdf.getPage(page));

                // Для каждой страницы с текстом вызываю метод
                this.parsePageText(file, page, text);
            }
        }
    }

    private void parsePageText(File pdfName, int page, String text) {
        var words = text.split("\\P{IsAlphabetic}+");

        Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота

        // Для каждого слова на странице
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            // Привожу в нижний регистр
            word = word.toLowerCase();
            // Для каждого слова получаю текущее значение в мапе, если его нет получаю ноль, инкрементирую значение на 1
            freqs.put(word, freqs.getOrDefault(word, 0) + 1);
        }

        // Обходя полученный мап с частотой употребления слов
        for (Entry<String, Integer> entry : freqs.entrySet()) {
            var currentWord = entry.getKey();
            
            // Проверяю, если ключа в мапе нет
            if(!wordsList.containsKey(currentWord)) {
                wordsList.put(currentWord, new ArrayList<>());
            }

            // Далее, получаю лист для данного слова
            var list = wordsList.get(currentWord);
            
            // Инициализирую переменную записи для текущего слова, документа и страницы
            PageEntry myEntry = null;
            // Ищу эту запись в листе
            for(PageEntry pageEntry : list) {
                if(pageEntry.getPdfName().equals(pdfName.getName()) && pageEntry.getPage() == page) {
                    myEntry = pageEntry;
                    break;
                }
            }

            // Если запись не была найдена, создаю
            if(myEntry == null) {
                myEntry = new PageEntry(pdfName, page);
                System.out.println(currentWord + " " + myEntry);
            }

            // Инкрементирую значение
            myEntry.setCount(myEntry.getCount() + entry.getValue());

            // Здесь пригождается реализованный equals
            if(!list.contains(myEntry)) {
                // Добавляю если объекта в листе не было
                list.add(myEntry);
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntryList = this.wordsList.getOrDefault(word, Collections.emptyList());
        Collections.sort(pageEntryList);
        return pageEntryList;
    }
}
