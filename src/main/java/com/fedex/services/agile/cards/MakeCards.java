package com.fedex.services.agile.cards;

import com.fedex.services.agile.cards.dao.FileTaskDao;
import com.fedex.services.agile.cards.dao.TaskDao;
import com.fedex.services.agile.cards.service.APICardService;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.IOException;

@CommonsLog
public class MakeCards {
	private void process(String filename) throws DRException, IOException {
		TaskDao dao = new FileTaskDao(filename);
		APICardService service = new APICardService();
		service.process(dao.retrieveJson(), null);
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
