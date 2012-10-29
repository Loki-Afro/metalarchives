package de.loki.metallum.entity;

public class Review extends AbstractEntity {

	private String	author			= "";
	private int		percent			= 0;
	private String	content			= "";
	private String	date			= "";
	private Disc	discFromReview	= new Disc();

	public Review() {
		super(0);
	}

	public void setPercent(final int percent) {
		this.percent = percent;
	}

	public int getPercent() {
		return this.percent;
	}

	public String getContet() {
		return this.content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return this.date;
	}

	public void setDisc(final Disc disc) {
		this.discFromReview = disc;
	}

	public Disc getDisc() {
		return this.discFromReview;
	}

}
