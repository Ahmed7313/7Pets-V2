// Generated code from Butter Knife. Do not modify!
package com.noon.a7pets;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderPlaced_ViewBinding implements Unbinder {
  private OrderPlaced target;

  @UiThread
  public OrderPlaced_ViewBinding(OrderPlaced target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OrderPlaced_ViewBinding(OrderPlaced target, View source) {
    this.target = target;

    target.orderidview = Utils.findRequiredViewAsType(source, R.id.orderid, "field 'orderidview'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderPlaced target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.orderidview = null;
  }
}
