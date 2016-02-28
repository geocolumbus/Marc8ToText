package org.oclc.campbelg.marc8.reader.record;

/**
 * Holds both views of a record
 */
public class Marc8Record {
    private CardView cardView;
    private RawView rawView;
    private RawBytes rawBytes;

    public Marc8Record(RawBytes rawBytes) {
        this.rawBytes = rawBytes;
        this.cardView = new CardView();
        this.rawView=new RawView();
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public RawView getRawView() {
        return rawView;
    }

    public void setRawView(RawView rawView) {
        this.rawView = rawView;
    }

    public RawBytes getRawBytes() {
        return rawBytes;
    }

    public void setRawBytes(RawBytes rawBytes) {
        this.rawBytes = rawBytes;
    }
}
