# ToggleView

		<com.example.toggleview.view.ToggleView
        android:id="@+id/tv"
        android:layout_width="wrap_content"
        app:toggle_background="@drawable/slide_bg"
        app:toggle_slideBlock="@drawable/slide_block"
        android:layout_height="wrap_content"/>

	<resources>
    <declare-styleable name="ToggleView">
        <attr name="toggle_background" format="reference"/>
        <attr name="toggle_slideBlock" format="reference"/>
        <attr name="textSize" format="dimension"/>
    </declare-styleable>
	</resources>

![](http://i.imgur.com/EiAUsRe.gif)