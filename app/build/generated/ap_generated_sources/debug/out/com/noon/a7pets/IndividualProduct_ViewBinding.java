// Generated code from Butter Knife. Do not modify!
package com.noon.a7pets;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class IndividualProduct_ViewBinding implements Unbinder {
  private IndividualProduct target;

  @UiThread
  public IndividualProduct_ViewBinding(IndividualProduct target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public IndividualProduct_ViewBinding(IndividualProduct target, View source) {
    this.target = target;

    target.productimage = Utils.findRequiredViewAsType(source, R.id.productimage, "field 'productimage'", ImageView.class);
    target.productname = Utils.findRequiredViewAsType(source, R.id.productname, "field 'productname'", TextView.class);
    target.productprice = Utils.findRequiredViewAsType(source, R.id.productprice, "field 'productprice'", TextView.class);
    target.addToCart = Utils.findRequiredViewAsType(source, R.id.add_to_cart, "field 'addToCart'", TextView.class);
    target.buyNow = Utils.findRequiredViewAsType(source, R.id.buy_now, "field 'buyNow'", TextView.class);
    target.productdesc = Utils.findRequiredViewAsType(source, R.id.productdesc, "field 'productdesc'", TextView.class);
    target.quantityProductPage = Utils.findRequiredViewAsType(source, R.id.quantityProductPage, "field 'quantityProductPage'", EditText.class);
    target.addToWishlist = Utils.findRequiredViewAsType(source, R.id.add_to_wishlist, "field 'addToWishlist'", LottieAnimationView.class);
    target.customheader = Utils.findRequiredViewAsType(source, R.id.customheader, "field 'customheader'", EditText.class);
    target.custommessage = Utils.findRequiredViewAsType(source, R.id.custommessage, "field 'custommessage'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    IndividualProduct target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productimage = null;
    target.productname = null;
    target.productprice = null;
    target.addToCart = null;
    target.buyNow = null;
    target.productdesc = null;
    target.quantityProductPage = null;
    target.addToWishlist = null;
    target.customheader = null;
    target.custommessage = null;
  }
}
