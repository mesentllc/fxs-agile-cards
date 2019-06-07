package com.fedex.services.agile.cards.model;

import java.awt.Color;
import java.util.regex.Pattern;

public class TaskModel implements Comparable {
	private static final Pattern SPACE_PATTERN = Pattern.compile("\\r|\\n|  ");

	private String acceptanceCriteria;
	private String userStoryId;
	private String userStoryTitle;
	private String task;
	private String taskId;
	private String description;
	private String hours;
	private String itg;
	private String priority;
	private String storyPoints;
	private String epic;
	private String feature;
	private String release;
	private String assignedTo;
	private String sprint;
	private Color color;

	public TaskModel() {
	}

	public String getAcceptanceCriteria() {
		return acceptanceCriteria;
	}

	public void setAcceptanceCriteria(String acceptanceCriteria) {
		this.acceptanceCriteria = acceptanceCriteria;
	}

	public String getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(String userStoryId) {
		this.userStoryId = userStoryId;
	}

	public String getUserStoryTitle() {
		return userStoryTitle;
	}

	public void setUserStoryTitle(String userStoryTitle) {
		this.userStoryTitle = userStoryTitle;
	}

	public String getTask() {
		return task;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getDescription() {
		if (description == null) {
			return "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHours() {
		return hours;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(String storyPoints) {
		this.storyPoints = storyPoints;
	}

	public String getEpic() {
		return epic;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getSprint() {
		return sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("User Story Id: ").append(userStoryId).append("\n")
				.append("User Story Title: ").append(userStoryTitle).append("\n")
				.append("ITG: ").append(itg).append("\n")
				.append("Priority: ").append(priority).append("\n")
				.append("Task Id: ").append(taskId).append("\n")
				.append("Task: ").append(task).append("\n")
				.append("Hours: ").append(hours).append("\n")
				.append("Story Points: ").append(storyPoints).append("\n")
				.append("Description: ").append(description).append("\n")
				.append("Acceptance Criteria: ").append(acceptanceCriteria).append("\n")
				.append("Epic: ").append(epic).append("\n")
				.append("Feature: ").append(feature).append("\n")
				.append("Release: ").append(release).append("\n")
				.append("Assigned To: ").append(assignedTo).toString();
	}

	@Override
	public int compareTo(Object o) {
		try {
			String strippedInput = SPACE_PATTERN.matcher(((TaskModel)o).getDescription()).replaceAll(" ");
			String strippedThis = SPACE_PATTERN.matcher(getDescription()).replaceAll(" ");
			return strippedThis.compareToIgnoreCase(strippedInput);
		}
		catch (NullPointerException npe) {
			return 1;
		}
	}
}
