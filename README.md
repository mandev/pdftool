# PdfTool

PdfTool is a Java library that offers filters to handle PDF files.

## Filters

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

## Render objects

* BarCode : draw a barcode
* Image : insert an image
* Line : a line
* PageMark : a page mark
* Rectangle : a rectangle
* Template : a PDF file 
* Text : some text
* WaterMark.java : a watermark

## Flow filter operators

* GroupFilter.java : flow operator to group filters
* Multiply.java : multiply the PDF flow (useful for impose filter)
* SelectPage.java : a page selector to operate on specific pages

## Top level objects

* PdfTool : add and execute PDF filers  
* PdfExtractor:  extract information from the PDF

## Code sample 

* Create a simple PDF : 

```
RenderFilter rf = new RenderFilter("10");
rf.addRender(new Text("Hello World", 8 * MMf, -5 * MMf - 12f));
rf.addRender(new Rectangle(5 * MMf, 5 * MMf, -5 * MMf, -5 * MMf));
rf.addRender(new BarCode("14567890111", 78 * MMf, 10 * MMf, 1f, 1f, 90, BarCode.ALIGN_CENTER));
rf.addRender(new Image(TEST_RESOURCES + "image.jpg", 10 * MMf, 10 * MMf, 0, 0, 1, BaseColor.CYAN));

PdfTool pdfTool = new PdfTool();
pdfTool.addFilter(new Create(ouputfile.getPath(), 210 * MMf, 210 * MMf, 2));
pdfTool.addFilter(rf);
pdfTool.execute();
```
 
* Impose a PDF file :

```
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
```

## XML 

The filters can be exported to XML. It's also possible to import the filters from an XML file and then execute the operations. 

* XML sample :

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<PdfTool>
  <GroupFilter deleteSource="false" count="0">
    <InsertPage deleteSource="false" destination="/tmp/pdftool/page_{TEMP}.tmp" pageCount="1" insertBefore="1"/>
    <DuplicatePage deleteSource="false" destination="/tmp/pdftool/plate_{TEMP}.tmp" count="5" pattern="2-"/>
    <Multiply deleteSource="false" count="2"/>
    <Impose deleteSource="true" destination="/tmp/pdftool/plate_{TEMP}.pdf" width="472.0 mm" height="315.0 mm" type="2">
      <PageSlot x="10.0 mm" y="10.0 mm" folio="true" folioX="110.0 mm" folioY="309.0 mm" folioText="{FILENAME} - Page {FOLIO}" folioSize="10"/>
      <PageSlot x="246.0 mm" y="10.0 mm" folio="true" folioX="330.0 mm" folioY="309.0 mm" folioText="Page {FOLIO}" folioSize="10"/>
      <PageMark x="3.0 mm" y="3.0 mm" size="5.0 mm" offset="5.0 mm" position="++||" thickness="0.35 mm">
        <color value="-16777216"/>
      </PageMark>
      <PageMark x="3.0 mm" y="3.0 mm" size="5.0 mm" offset="5.0 mm" position="||++" thickness="0.35 mm">
        <color reference="../../PageMark/color"/>
      </PageMark>
    </Impose>
  </GroupFilter>
  <GroupFilter deleteSource="false" count="3">
    <RemovePage deleteSource="false" destination="/tmp/pdftool/page_{TEMP}.tmp" pattern="1"/>
    <Multiply deleteSource="false" count="2"/>
    <Impose reference="../../GroupFilter/Impose"/>
  </GroupFilter>
</PdfTool>
```

## Build 
 
Java 8+ is mandatory to build emailcruncher.

```
mvn clean install
```

## Contribute

Contributions are welcome.

1. Fork the project on Github (git clone ...)
2. Create a local feature branch (git checkout -b newFeature)
3. Commit modifications on the local feature branch (git commit -am "new modification")
4. Push the local branch (git push origin newFeature)
5. Create a pull request


## License

PdfTool is based on iText and is licensed as AGPL. It means it's free for non commercial use only.
