package com.fedex.services.agile.cards.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.services.agile.cards.model.TaskModel;
import com.fedex.services.agile.cards.model.V1Asset;
import com.fedex.services.agile.cards.model.V1Object;
import com.fedex.services.agile.cards.model.V1Value;
import com.fedex.services.agile.cards.model.V1Values;
import com.fedex.services.agile.cards.report.CardReport;
import com.fedex.services.agile.cards.report.V1CardCreator;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.jsoup.Jsoup;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
public class APICardService {
	private static ObjectMapper mapper = new ObjectMapper();
	private static final Color[] COLORS
			= {Color.YELLOW, Color.ORANGE, new Color(102, 255, 102),
				new Color(102, 178, 255), new Color(192, 192, 192), new Color(178, 102, 255), Color.WHITE,
				Color.RED, Color.BLACK, new Color(0, 102, 0), new Color(102, 51, 0),
				new Color(204, 0, 204), new Color(153, 0, 76)};

	public void process(String json, String outFile) throws DRException, IOException {
		V1Object v1Response = mapper.readValue(json, V1Object.class);
		List<TaskModel> cards = convert(v1Response);
//		Collections.sort(cards);
		CardReport cardReport = new V1CardCreator();
		JasperReportBuilder report = cardReport.buildCards(cards);
		if (outFile != null && outFile.length() > 0) {
			if (!outFile.toLowerCase().endsWith(".pdf")) {
				outFile += ".pdf";
			}
			report.toPdf(new FileOutputStream(outFile));
		} else {
			report.print();
		}
	}

	private List<TaskModel> convert(V1Object v1Response) {
		List<TaskModel> cards = new ArrayList<>();
		Map<String, Color> colorMap = new HashMap<>();
		for (V1Asset asset : v1Response.getAssets()) {
			TaskModel model = new TaskModel();
//			model.setAcceptanceCriteria(acceptanceCriteria);
			model.setAssignedTo(linkValues(asset.getAttribute().getOwners()));
			model.setDescription(safeValue(asset.getAttribute().getDescription()));
//			model.setEpic(epic);
			String featureNumber = safeValue(asset.getAttribute().getSuperNumber());
			model.setFeature(featureNumber + " - " + safeValue(asset.getAttribute().getSuperName()));
//			model.setHours(hours);
//			model.setItg(itg);
			model.setPriority(safeValue(asset.getAttribute().getPriority()));
//			model.setRelease(release);
			model.setStoryPoints(safeValue(asset.getAttribute().getPoints()));
//			model.setTask(task);
//			model.setTaskId(teskId);
			model.setUserStoryId(safeValue(asset.getAttribute().getNumber()));
			model.setUserStoryTitle(safeValue(asset.getAttribute().getName()));
			model.setColor(getColorFromMap(featureNumber, colorMap));
			cards.add(model);
		}
		return cards;
	}

	private Color getColorFromMap(String featureNumber, Map<String, Color> colorMap) {
		if (!colorMap.containsKey(featureNumber)) {
			colorMap.put(featureNumber, COLORS[colorMap.size() % COLORS.length]);
		}
		return colorMap.get(featureNumber);
	}

	private String safeValue(V1Value v1Value) {
		if (v1Value == null || v1Value.getValue() == null) {
			return "";
		}
		return Jsoup.parse(v1Value.getValue()).text();
	}

	private String linkValues(V1Values owners) {
		StringBuilder sb = new StringBuilder();
		if (owners == null) {
			return "";
		}
		for (String name : owners.getValue()) {
			if (name != null && name.trim().length() > 0) {
				sb.append(name).append(',');
			}
		}
		if (sb.length() > 0) {
			return sb.toString().substring(0, sb.length() - 1);
		}
		return "";
	}
}
