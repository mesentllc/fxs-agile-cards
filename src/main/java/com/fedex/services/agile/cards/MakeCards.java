package com.fedex.services.agile.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.services.agile.cards.dao.FileTaskDao;
import com.fedex.services.agile.cards.model.TaskModel;
import com.fedex.services.agile.cards.model.V1Object;
import com.fedex.services.agile.cards.service.APICardService;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.IOException;
import java.util.List;

@CommonsLog
public class MakeCards {
	private void process(String filename) throws DRException, IOException {
		FileTaskDao dao = new FileTaskDao();
		ObjectMapper mapper = new ObjectMapper();
		V1Object v1Response = mapper.readValue(dao.retrieveJson(filename), V1Object.class);
		List<TaskModel> storyList = new APICardService().convert(v1Response);
		APICardService.process(storyList, null);
	}

	public static void main(String[] args) throws DRException, IOException {
		if (args.length == 1) {
			MakeCards makeCards = new MakeCards();
			makeCards.process(args[0]);
			System.exit(0);
		}
		throw new RuntimeException("Need to supply the file to use.");
	}
}
