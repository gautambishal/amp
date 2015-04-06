package org.digijava.kernel.ampapi.endpoints.postgis;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.TextNode;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureCollectionGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.ampapi.postgis.util.ScoreCalculator;

@Path("postgis")
public class PostgisEndPoints {

	private static final Logger logger = Logger.getLogger(PostgisEndPoints.class);
	private static final int MAX_DISTANCE_METERS = 5 * 1000;

	@GET
	@Path("/location/{locationName}/{includeCloseBy}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public FeatureCollectionGeoJSON locations(@PathParam("locationName") String locationName,@PathParam ("includeCloseBy") Boolean includeCloseBy) {
		List<AmpLocator> locations = QueryUtil.getLocationsFromKeyword(locationName.toLowerCase());
		List <Long> idLists = QueryUtil.getIdsList(locations);
		FeatureCollectionGeoJSON featureCollection = new FeatureCollectionGeoJSON();
		for (AmpLocator locator : locations) {
			double score = ScoreCalculator.getScore(locator.getAnglicizedName(), locator.getAnglicizedName(), locator.getDistance());
			featureCollection.features.add(getLocation(Double.valueOf(locator.getLatitude()),
					Double.valueOf(locator.getLongitude()), locator.getName(), score));
			if (includeCloseBy && locator.getDistance() == 0) {
				List <AmpLocator> closeByLocations = QueryUtil.getLocationsWithinDistance(locator.getTheGeometry(), MAX_DISTANCE_METERS,idLists);
				for (AmpLocator closeBy : closeByLocations) {
					FeatureGeoJSON json = getLocation(Double.valueOf(closeBy.getLatitude()),
							Double.valueOf(closeBy.getLongitude()), closeBy.getName(), 75);
					json.properties.put("isCloseBy", new TextNode("true"));
					featureCollection.features.add(json);
				}
				
			}
		}
		Collections.sort(featureCollection.features, new Comparator<FeatureGeoJSON>() {
			@Override
			public int compare(FeatureGeoJSON o1, FeatureGeoJSON o2) {
				Double score1 = o1.properties.get("score").asDouble();
				Double score2 = o1.properties.get("score").asDouble();
				return score1.compareTo(score2);
			}
		});
		return featureCollection;
	}

	private FeatureGeoJSON getLocation(Double lat, Double lon, String name, double score) {
		FeatureGeoJSON fgj = new FeatureGeoJSON();
		PointGeoJSON pg = new PointGeoJSON();
		pg.coordinates.add(lon);
		pg.coordinates.add(lat);
		fgj.properties.put("name", new TextNode(name));
		fgj.properties.put("score", new DoubleNode(score));
		fgj.geometry = pg;
		return fgj;
	}

}
