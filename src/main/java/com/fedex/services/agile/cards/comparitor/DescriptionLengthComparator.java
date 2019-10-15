package com.fedex.services.agile.cards.comparitor;

import com.fedex.services.agile.cards.model.TaskModel;

import java.util.Comparator;

public class DescriptionLengthComparator implements Comparator<TaskModel> {
	@Override
	public int compare(TaskModel item1, TaskModel item2) {
		return item1.getDescription().length() - item2.getDescription().length();
	}
}
