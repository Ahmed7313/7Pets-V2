// Generated code from Butter Knife. Do not modify!
package com.noon.a7pets;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderDetails_ViewBinding implements Unbinder {
  private OrderDetails target;

  @UiThread
  public OrderDetails_ViewBinding(OrderDetails target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OrderDetails_ViewBinding(OrderDetails target, View source) {
    this.target = target;

    target.deliveryDate = Utils.findRequiredViewAsType(source, R.id.delivery_date, "field 'deliveryDate'", TextView.class);
    target.noOfItems = Utils.findRequiredViewAsType(source, R.id.no_of_items, "field 'noOfItems'", TextView.class);
    target.totalAmount = Utils.findRequiredViewAsType(source, R.id.total_amount, "field 'totalAmount'", TextView.class);
    target.ordername = Utils.findRequiredViewAsType(source, R.id.ordername, "field 'ordername'", MaterialEditText.class);
    target.orderemail = Utils.findRequiredViewAsType(source, R.id.orderemail, "field 'orderemail'", MaterialEditText.class);
    target.ordernumber = Utils.findRequiredViewAsType(source, R.id.ordernumber, "field 'ordernumber'", MaterialEditText.class);
    target.orderaddress = Utils.findRequiredViewAsType(source, R.id.orderaddress, "field 'orderaddress'", MaterialEditText.class);
    target.orderpincode = Utils.findRequiredViewAsType(source, R.id.orderpincode, "field 'orderpincode'", MaterialEditText.class);
    target.rGroup = Utils.findRequiredViewAsType(source, R.id.rGroup, "field 'rGroup'", RadioGroup.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderDetails target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.deliveryDate = null;
    target.noOfItems = null;
    target.totalAmount = null;
    target.ordername = null;
    target.orderemail = null;
    target.ordernumber = null;
    target.orderaddress = null;
    target.orderpincode = null;
    target.rGroup = null;
  }
}
