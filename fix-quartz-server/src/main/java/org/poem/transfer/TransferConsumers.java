package org.poem.transfer;

/**
 * @author Administrator
 */
public interface TransferConsumers {


    /**
     * @param transferInfo
     * @return
     */
    TransferRequest execute(TransferInfo transferInfo);

}
