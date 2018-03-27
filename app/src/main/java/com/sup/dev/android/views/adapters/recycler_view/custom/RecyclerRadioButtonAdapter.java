package com.sup.dev.android.views.adapters.recycler_view.custom;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerArrayAdapter;

public class RecyclerRadioButtonAdapter<K> extends RecyclerArrayAdapter<K> {

    private ItemMaskProvider<K> itemMaskProvider;
    private OnSelectedChangeListener<K> onSelectedChangeListener;
    private K selectedItem;

    public RecyclerRadioButtonAdapter() {
        super(R.layout.row_radiobutton);
    }

    @Override
    protected void bind(View view, K item) {

        TextView text = (TextView) view.findViewById(R.id.text);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radio_button);
        View touch = view.findViewById(R.id.touch);

        touch.setOnClickListener(v -> onChecked(item));
        text.setText((itemMaskProvider == null) ? item.toString() : itemMaskProvider.maskFor(item));
        radioButton.setChecked(selectedItem == item);
    }

    private void onChecked(K item) {
        setSelectedItem(item);
        if (onSelectedChangeListener != null)
            onSelectedChangeListener.onSelectedChange(item);
    }

    public void setSelectedItem(K selectedItem) {
        int oldIndex = indexOf(this.selectedItem);
        this.selectedItem = selectedItem;

        if (oldIndex != -1) notifyItemChanged(oldIndex);
        if (selectedItem != null) notifyItemChanged(indexOf(selectedItem));
    }

    public void setItemMaskProvider(ItemMaskProvider<K> itemMaskProvider) {
        this.itemMaskProvider = itemMaskProvider;
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener<K> onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public interface ItemMaskProvider<K> {

        String maskFor(K item);

    }

    public interface OnSelectedChangeListener<K> {

        void onSelectedChange(K item);

    }

}
