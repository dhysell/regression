package repository.cc.entities;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;

import java.math.BigDecimal;

public class CheckLineItem {

    private repository.cc.enums.CheckLineItemType type;
    private repository.cc.enums.CheckLineItemCategory category;
    private String comments;
    private BigDecimal amount;

    public CheckLineItem() {

    }

    public CheckLineItem(repository.cc.enums.CheckLineItemType type, repository.cc.enums.CheckLineItemCategory category, BigDecimal amount) {
        this.type = type;
        this.category = category;
        this.comments = "Check Line Item Comment";
        this.amount = amount;
    }

    public repository.cc.enums.CheckLineItemType getType() {
        return type;
    }

    public void setType(CheckLineItemType type) {
        this.type = type;
    }

    public repository.cc.enums.CheckLineItemCategory getCategory() {
        return category;
    }

    public void setCategory(CheckLineItemCategory category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
