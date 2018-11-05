package vbee.bookcmsbackend.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/**
 *
 * @author danhh
 */

public class ParagraphStyle implements Serializable {

    @Id
   private Integer paraID = -1;

	private Boolean isBold = false;
	private Boolean isItalic = false;
	private Integer fontSize = -1;
	private String fontName = "";
	private String text= "";
	private Integer order = -1;
	private String STYLE= "";
	
	public ParagraphStyle(){
		
	}
	public ParagraphStyle(Boolean isBold, Boolean isItalic, Integer fontSize, String fontName) {
		super();
		this.isBold = isBold;
		this.isItalic = isItalic;
		this.fontSize = fontSize;
		this.fontName = fontName;
	}
	public String getSTYLE() {
		return STYLE;
	}
	public void setSTYLE(String sTYLE) {
		STYLE = sTYLE;
	}
	public String toKey() {
		return this.isBold.toString()+";"+this.isItalic.toString()+";"+this.fontSize.toString()+";"+this.getFontName();
	}

	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getIsBold() {
		return isBold;
	}

	public void setIsBold(Boolean isBoid) {
		this.isBold = isBoid;
	}

	public Boolean getIsItalic() {
		return isItalic;
	}

	public void setIsItalic(Boolean isItalic) {
		this.isItalic = isItalic;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

    public Integer getParaID() {
        return paraID;
    }

    public void setParaID(Integer paraID) {
        this.paraID = paraID;
    }
    
}
