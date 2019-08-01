package org.poem.transfer;

/**
 * @author Administrator
 */
public interface TransferConsumers {


    /**
     * transferInfo
     * @param transferInfo
     * @return
     */
    TransferRequest execute(TransferInfo transferInfo);

}
