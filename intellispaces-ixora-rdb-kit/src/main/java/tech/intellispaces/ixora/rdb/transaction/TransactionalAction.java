package tech.intellispaces.ixora.rdb.transaction;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.action.wrapper.AbstractWrapperAction;

public class TransactionalAction extends AbstractWrapperAction {
  private final MovableTransactionFactory transactionFactory;

  public TransactionalAction(MovableTransactionFactory transactionFactory, Action wrappedAction) {
    super(wrappedAction);
    this.transactionFactory = transactionFactory;
  }

  @Override
  public Object execute(Object... data) {
    return TransactionFunctions.transactional(transactionFactory, wrappedAction()::execute, data);
  }
}
