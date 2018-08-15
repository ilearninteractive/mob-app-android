package sa.gov.moe.etraining.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import sa.gov.moe.etraining.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public LoadingViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_view_footer_progress, parent, false));
    }
}
