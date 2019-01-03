package repository.cc.framework.gw.element;

import org.openqa.selenium.By;
import repository.cc.framework.gw.element.enums.ElementType;

public class Identifier {
    private By reference;
    private ElementType elementType;

    public Identifier(By reference) {
        this.reference = reference;
    }

    public Identifier(By reference, ElementType elementType) {
        this.reference = reference;
        this.elementType = elementType;
    }

    public By getReference() {
        return reference;
    }

    public ElementType getElementType() {
        return elementType;
    }
}
