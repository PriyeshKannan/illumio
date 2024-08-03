package com.illumio.aggregator;

import com.illumio.IllumioException;
import com.illumio.data.Data;
import com.illumio.flowlog.record.FlowRecord;

public interface Aggregator {

    /**
     * The processed data that can be persisted or passed to another aggregation source.
     *
     * @param flowRecord the parsed FlowLog Record
     * @return
     */
    Data analyze(FlowRecord flowRecord);

    /**
     * Create a in-memory store. we can period flush using persist.
     * @param data the parse flow dara
     */
    void capture(Data data);
    /**
     * Persist to external source like database  or file to store the last snapshot
     */
    void persist() throws IllumioException;

}
