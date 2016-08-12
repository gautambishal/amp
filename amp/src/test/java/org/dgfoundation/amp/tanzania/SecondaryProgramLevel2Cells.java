package org.dgfoundation.amp.tanzania;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedCells;

import org.dgfoundation.amp.nireports.PercentageTextCell;


public class SecondaryProgramLevel2Cells extends HardcodedCells<PercentageTextCell>{

	public SecondaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
		super(activityNames, entityNames, degenerate(dim, key));
	}
	public SecondaryProgramLevel2Cells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<PercentageTextCell> populateCells() {
		return 	Arrays.asList(
);
	}

}
