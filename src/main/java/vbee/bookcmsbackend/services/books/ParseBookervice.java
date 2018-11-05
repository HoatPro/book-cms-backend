package vbee.bookcmsbackend.services.books;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.models.ParagraphStyle;
import vbee.bookcmsbackend.utils.TextUtils;

@Service
public class ParseBookervice implements IParseBookService {

	private static final Logger logger = LoggerFactory.getLogger(ParseBookervice.class);

	@Override
	public List<Book> parseExcelFile(FileInputStream excelFile) {
		List<Book> listBook = new ArrayList<Book>();
		try {
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			int countRow = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (countRow == 0) {
					countRow++;
					continue;
				}
				Book newBook = new Book();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				countRow++;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						switch (cell.getColumnIndex()) {
						case 3:
							// public month
							Double month = cell.getNumericCellValue();
							newBook.setPublicMonth(month.intValue());
							break;
						case 4:
							// public year
							Double year = cell.getNumericCellValue();
							newBook.setPublicYear(year.intValue());
							break;
						case 5:
							// total pages
							Double pages = cell.getNumericCellValue();
							newBook.setPageNumber(pages.intValue());
							break;
						}
					case Cell.CELL_TYPE_STRING:
						switch (cell.getColumnIndex()) {
						case 0:
							// title
							newBook.setTitle(cell.getStringCellValue());
						case 1:
							// translator
							newBook.setTranslator(cell.getStringCellValue());
							break;
						case 2:
							// publishing company
							newBook.setPublishingCompany(cell.getStringCellValue());
							break;
						case 6:
							// file name
							newBook.setFileName(cell.getStringCellValue());
							break;
						}
					}
				}
				if (!newBook.getTitle().equals("Tên Sách")) {
					newBook.setStatusId(AppConstant.NO_CONTENT);
					listBook.add(newBook);
				}
			}
			excelFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("error when parse excel file !!!");
		}
		return listBook;
	}

	private static int countChar(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	@Override
	public List<Chapter> getBookContent(List<ParagraphStyle> listParaStyle, String bookId, String email,
			String ownerEmail, Boolean isCreate) {

		StringBuilder sbChapterContent = new StringBuilder();
		StringBuilder sbTitleChapter = new StringBuilder();
		Chapter chapter = null;
		if (isCreate)
			chapter = new Chapter(bookId, email, ownerEmail);
		else
			chapter = new Chapter();
		List<Chapter> chapters = new ArrayList<>();
		if (listParaStyle.isEmpty()) {
			return chapters;
		}
		int chapterOrder = 0;
		boolean first = false;
		boolean first_content = false;
		// Đoạn văn bắt đầu bằng heading1
		ParagraphStyle firstParagraph = listParaStyle.get(0);
		if (TextUtils.toKey(firstParagraph.getSTYLE()).contains("heading1")) {
			sbTitleChapter.append(firstParagraph.getText().trim()).append(" ");
			first = true;
		} else
			sbChapterContent.append(listParaStyle.get(0).getText().trim());
		// Bắt đầu vòng lặp từ i = 1
		for (int i = 1; i < listParaStyle.size(); i++) {
			String prTextPrev = listParaStyle.get(i - 1).getText().trim();
			ParagraphStyle pr = listParaStyle.get(i);
			String prText = pr.getText().trim();
			// Đoạn văn không phải là heading 1
			if (!TextUtils.toKey(pr.getSTYLE()).contains("heading1")) {
				// Xác định đoạn văn có phải là đoạn văn hoàn chỉnh hay không
				char cf = prText.charAt(0);
				// Đoạn văn chứa ký tự mở đầu (chữ cái đầu tiên viết hoa, hoặc số, hoặc ký tự
				// lạ)
				if (Character.isUpperCase(cf) || Character.isDigit(cf) || !Character.isLetterOrDigit(cf)) {
					sbChapterContent.append("\n\n").append(prText);
				}
				// Đoạn văn không chưa ký tự mở đầu
				else if (sbChapterContent.length() > 0) {
					char ce = sbChapterContent.charAt(sbChapterContent.length() - 1);
					// Đoạn văn chứa ký tự kết thúc là dấu đóng mở (vd: '', "", []...) thì kiểm tra
					// trong đoạn văn
					// trước đó có đã chứa dấu đó bao nhiêu lần.
					if (ce == '"' || ce == '\'' || ce == '(' || ce == '[' || ce == '{') {
						int count = countChar(prTextPrev, ce);
						if (count > 0 && count % 2 == 0) { // Đoạn trước chứa đủ dấu câu -> Đoạn hiện tại là đoạn mới
							sbChapterContent.append("\n\n").append(prText);
						} else {
							// Đoạn văn là đoạn không hoàn chỉnh. nối với đoạn văn trước đó
							sbChapterContent.append(" ").append(prText);
						}
					}
					// Đoạn văn là đoạn hoàn chỉnh
					else if (ce != '-' && ce != ':' && ce != ',' && !Character.isLetterOrDigit(ce)) {
						sbChapterContent.append("\n\n").append(prText);
					}
				} else { // Những trường hợp còn lại thì nối tiếp vào đoạn trước đó
					sbChapterContent.append(" ").append(prText);
				}
				first_content = true;
			} else {
				// vào lần đầu khi là heading1 để add content chapter cũ và tạo chapter mới
				if (first_content) {
					chapter.setContent(sbChapterContent.toString().trim());
					chapters.add(chapter);
					if (isCreate)
						chapter = new Chapter(bookId, email, ownerEmail);
					else
						chapter = new Chapter();
					sbChapterContent.setLength(0);
				}
				first_content = false;
			}

			if (TextUtils.toKey(pr.getSTYLE()).contains("heading1")) {
				first = true;
				sbTitleChapter.append(prText).append(" ");
			} else {
				// vào lần đầu khi không phải là heading1, set title của chapter hiện tại
				if (first) {
					chapterOrder++;
					chapter.setTitle(sbTitleChapter.toString().trim());
					chapter.setOrderNo(chapterOrder);
					sbTitleChapter.setLength(0);
				}
				first = false;
			}
		}

		// add last chapter
		chapter.setContent(sbChapterContent.toString().trim());
		chapters.add(chapter);
		return chapters;
	}

	@Override
	public List<ParagraphStyle> readDocx(FileInputStream input) throws IOException {
		XWPFDocument docx = new XWPFDocument(input);
		List<XWPFParagraph> listPara = docx.getParagraphs();
		List<ParagraphStyle> listStylePara = new ArrayList<>();
		XWPFStyles styles = docx.getStyles();
		XWPFWordExtractor we = new XWPFWordExtractor(docx);
		boolean start = false;
		int order = 0;
		for (XWPFParagraph pr : listPara) {
			String prText = pr.getText().trim().replaceAll("[\r\n]+$", " ");
			prText = prText.replaceAll("\n", "");
			ParagraphStyle paraStyle = new ParagraphStyle();

			// not get para ulti meet <nsw-book> pharse
			// not get para which is white line
			// not get para contains symbol or text of page number
			if (prText.contains("<end-book>")) {
				start = false;
				continue;
				// end book
			}

			if (prText.isEmpty()) {
				// clean white line
				continue;
			}
			if (start) {
				if (prText.matches("^.*[" + AppConstant.LATIN_LETTERS + "0-9].*$")) {
					XWPFStyle style = styles.getStyle(pr.getStyleID());
					if (style == null) {
						paraStyle.setSTYLE("Normal");
					} else {
						paraStyle.setSTYLE("" + style.getName());
					}
					// normalization text
					prText = prText.replaceAll("(…)+", "...");
					prText = prText.replaceAll("(\\.\\.\\.)+", "...");
					paraStyle.setOrder(order);
					paraStyle.setText(prText);
					listStylePara.add(paraStyle);
					order++;
				}
			}
			if (prText.contains("<start-book>")) {
				start = true;
				continue;
				// start get paragraph
			}

		}
		return listStylePara;
	}

}
