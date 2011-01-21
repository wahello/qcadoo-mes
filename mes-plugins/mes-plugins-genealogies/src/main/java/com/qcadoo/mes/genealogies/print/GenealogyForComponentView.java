package com.qcadoo.mes.genealogies.print;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.qcadoo.mes.api.DataDefinitionService;
import com.qcadoo.mes.api.Entity;
import com.qcadoo.mes.api.SecurityService;
import com.qcadoo.mes.beans.users.UsersUser;
import com.qcadoo.mes.model.search.Restrictions;
import com.qcadoo.mes.utils.pdf.PdfUtil;
import com.qcadoo.mes.utils.pdf.ReportPdfView;

public class GenealogyForComponentView extends ReportPdfView {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DataDefinitionService dataDefinitionService;

    @Override
    protected String addContent(final Document document, final Object value, final Locale locale, final PdfWriter writer)
            throws DocumentException, IOException {
        Entity entity = dataDefinitionService.get("genealogies", "productInBatch").get(Long.valueOf(value.toString()));
        String documentTitle = getTranslationService().translate("genealogies.genealogyForComponent.report.title", locale);
        String documentAuthor = getTranslationService().translate("genealogies.genealogyForComponent.report.author", locale);
        UsersUser user = securityService.getCurrentUser();
        PdfUtil.addDocumentHeader(document, entity.getField("batch").toString(), documentTitle, documentAuthor, new Date(), user);
        addTables(document, entity, locale);
        String text = getTranslationService().translate("core.report.endOfReport", locale);
        PdfUtil.addEndOfDocument(document, writer, text);
        return getTranslationService().translate("genealogies.genealogyForComponent.report.fileName", locale);
    }

    @Override
    protected void addTitle(final Document document, final Locale locale) {
        document.addTitle(getTranslationService().translate("genealogies.genealogyForComponent.report.title", locale));
    }

    private void addTables(final Document document, final Entity entity, final Locale locale) throws DocumentException {
        document.add(Chunk.NEWLINE);
        List<String> orderHeader = new ArrayList<String>();
        orderHeader.add(getTranslationService().translate("products.order.number.label", locale));
        orderHeader.add(getTranslationService().translate("products.order.name.label", locale));
        orderHeader.add(getTranslationService().translate("products.order.product.label", locale));
        orderHeader.add(getTranslationService().translate("genealogies.genealogyForComponent.report.productBatch", locale));
        orderHeader.add(getTranslationService().translate("genealogies.genealogy.quantity.label", locale));
        orderHeader.add(getTranslationService().translate("products.order.dateFrom.label", locale));
        addOrderSeries(document, entity, orderHeader);

    }

    private void addOrderSeries(final Document document, final Entity entity, final List<String> orderHeader)
            throws DocumentException {
        PdfPTable table = PdfUtil.createTableWithHeader(6, orderHeader, false);
        List<Entity> genealogies = getGenealogies(entity);
        for (Entity genealogy : genealogies) {
            Entity order = (Entity) genealogy.getField("order");
            table.addCell(new Phrase(order.getField("number").toString(), PdfUtil.getArialRegular9Dark()));
            table.addCell(new Phrase(order.getField("name").toString(), PdfUtil.getArialRegular9Dark()));
            Entity product = (Entity) order.getField("product");
            if (product == null) {
                table.addCell(new Phrase("", PdfUtil.getArialRegular9Dark()));
            } else {
                table.addCell(new Phrase(product.getField("name").toString(), PdfUtil.getArialRegular9Dark()));
            }
            table.addCell(new Phrase(genealogy.getField("batch").toString(), PdfUtil.getArialRegular9Dark()));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            BigDecimal quantity = (BigDecimal) genealogy.getField("quantity");
            quantity = (quantity == null) ? BigDecimal.ZERO : quantity;
            table.addCell(new Phrase(getDecimalFormat().format(quantity), PdfUtil.getArialRegular9Dark()));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Phrase(order.getField("dateFrom").toString(), PdfUtil.getArialRegular9Dark()));
        }
        document.add(table);
    }

    private List<Entity> getGenealogies(final Entity entity) {
        List<Entity> genealogies = new ArrayList<Entity>();
        List<Entity> batchList = dataDefinitionService.get("genealogies", "productInBatch").find()
                .restrictedWith(Restrictions.eq("batch", entity.getField("batch"))).list().getEntities();
        for (Entity batch : batchList) {
            Entity genealogy = (Entity) ((Entity) ((Entity) batch.getField("productInComponent")).getField("genealogy"));
            if (!genealogies.contains(genealogy)) {
                genealogies.add(genealogy);
            }
        }
        return genealogies;
    }
}
