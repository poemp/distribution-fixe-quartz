package org.poem.transfer;

import java.util.List;

/**
 * @author Administrator
 */
public interface TransferConsumers {


    /**
     * transferInfo
     *
     * @param transferInfo
     * @return
     */
    List<TransferRequest> execute(TransferInfo transferInfo);

}
