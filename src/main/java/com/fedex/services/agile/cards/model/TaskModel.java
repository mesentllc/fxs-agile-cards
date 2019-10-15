package com.fedex.services.agile.cards.model;

import java.awt.Color;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

	public TaskModel(TaskModel original) {
		this.acceptanceCriteria = original.acceptanceCriteria;
		this.userStoryId = original.userStoryId;
		this.userStoryTitle = original.userStoryTitle;
		this.task = original.task;
		this.taskId = original.taskId;
		this.description = original.description;
		this.hours = original.hours;
		this.itg = original.itg;
		this.priority = original.priority;
		this.storyPoints = original.storyPoints;
		this.epic = original.epic;
		this.feature = original.feature;
		this.release = original.release;
		this.assignedTo = original.assignedTo;
		this.sprint = original.sprint;
		this.color = original.color;
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
