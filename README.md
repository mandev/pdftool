## pdftool

PdfTool offers many filters to retrieve information and operate on PD files.    

# Filters

* AddMargin : add a margin
* AddMetadata : add metadata 
* Concat : concat serveral pdf files 
* Copy : copy a pdf file  
* Create : create a pdf file  
* CropMargin : crop the pdf  
* CropMark : add crop mark
* Decrypt : decrypt the pdf 
* DuplicatePage : duplicate pages of the PDF
* Encrypt : encrypt the PDF
* Impose : impose the PDF with various strategies
* InsertPage : insert a page 
* Merge : merge two PDF into another
* PaddingPage : add a padding on a page
* Render : draw a renderable object (rectangle,line, marks, image, etc.)
* RemovePage : remove a page from the PDF
* Rename : rename the PDF
* Resize : resize (width, height) the PDF 
* ReversePage : reverse the pages in the PDF
* SplitDocument : split the PDF document
* SplitPage : spit each page of the PDF document
* Transform : transform with a 2d trsnformation

# Render objects

* BarCode : draw a barcode
* Image : insert an image
* Line : a line
* PageMark : a page mark
* Rectangle : a rectangle
* Template : a PDF file 
* Text : some text
* WaterMark.java : a watermark

# Flow filter operators

* GroupFilter.java : flow operator to group filters
* Multiply.java : multiply the PDF flow (useful for impose filter)
* SelectPage.java : a page selector to operate on specific pages

# Top level objects

* PdfTool : add and execute PDF filers  
* PdfExtractor:  extract information from the PDF

# Code sample 

Create a simple PDF : 
'''
        RenderFilter rf = new RenderFilter("10");
        rf.addRender(new Text("Hello World", 8 * MMf, -5 * MMf - 12f));
        rf.addRender(new Rectangle(5 * MMf, 5 * MMf, -5 * MMf, -5 * MMf));
        rf.addRender(new BarCode("14567890111", 78 * MMf, 10 * MMf, 1f, 1f, 90, BarCode.ALIGN_CENTER));
        rf.addRender(new Image(TEST_RESOURCES + "image.jpg", 10 * MMf, 10 * MMf, 0, 0, 1, BaseColor.CYAN));

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(new Create(ouputfile.getPath(), 210 * MMf, 210 * MMf, 2));
        pdfTool.addFilter(rf);
        pdfTool.execute();
  '''
 
Impose a PDF file :

'''
Impose impose = new Impose(TEST_RESULTS + "plate_{TEMP}.pdf", (20 + 216) * 2 * MMtoPT, (20 + 295) * MMtoPT, Impose.DIVIDE);
        impose.addPageSlot(new PageSlot(10 * MMtoPT, 10 * MMtoPT, true, 110 * MMtoPT, 309 * MMtoPT, "{FILENAME} - Page {FOLIO}", 10));
        impose.addPageSlot(new PageSlot(246 * MMtoPT, 10 * MMtoPT, true, 330 * MMtoPT, 309 * MMtoPT, "Page {FOLIO}", 10));
        impose.addPageMark(new PageMark(3 * MMtoPT, 3 * MMtoPT, 5 * MMtoPT, 5 * MMtoPT, "++||"));
        impose.addPageMark(new PageMark(3 * MMtoPT, 3 * MMtoPT, 5 * MMtoPT, 5 * MMtoPT, "||++"));
        impose.setDeleteSource(true);

        GroupFilter group1 = new GroupFilter();
        group1.addFilter(new InsertPage(TEST_RESULTS + "page_{TEMP}.tmp", 1, 1));
        group1.addFilter(new DuplicatePage(TEST_RESULTS + "plate_{TEMP}.tmp", "2-", 5));
        group1.addFilter(new Multiply(2));
        group1.addFilter(impose);

        GroupFilter group2 = new GroupFilter(3);
        group2.addFilter(new RemovePage(TEST_RESULTS + "page_{TEMP}.tmp", "1")); // SRA3 : 450x320 mm
        group2.addFilter(new Multiply(2));
        group2.addFilter(impose);

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(group1);
        pdfTool.addFilter(group2);

        File file1 = new File(TEST_RESOURCES + "julesverne.pdf");
        pdfTool.execute(file1);
'''

# Licensing

PdfTool is based on iText and is licensed as AGPL. Il means it's free for non commercial use.
