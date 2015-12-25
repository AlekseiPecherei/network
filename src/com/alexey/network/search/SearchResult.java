package com.alexey.network.search;

public class SearchResult {
	private String mName;
	private String mDiscription;
	private String mLink;

	public String getName() {
		return mName;
	}

	public String getDiscription() {
		return mDiscription;
	}

	public String getLink() {
		return mLink;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setDiscription(String discription) {
		mDiscription = discription;
	}

	public void setLink(String link) {
		mLink = link;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(mName).append(System.lineSeparator()).append(mDiscription).append(System.lineSeparator())
				.append(mLink).append(System.lineSeparator()).append("----------------------");
		return builder.toString();
	}
}
