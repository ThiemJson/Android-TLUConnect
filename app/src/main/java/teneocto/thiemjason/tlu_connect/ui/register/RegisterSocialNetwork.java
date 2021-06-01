package teneocto.thiemjason.tlu_connect.ui.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import teneocto.thiemjason.tlu_connect.R;
import teneocto.thiemjason.tlu_connect.models.SharedDTO;
import teneocto.thiemjason.tlu_connect.ui.adapter.RegisterAdapter;
import teneocto.thiemjason.tlu_connect.ui.bottomactionsheet.BottomSheetFragment;
import teneocto.thiemjason.tlu_connect.ui.drawer.Drawer;
import teneocto.thiemjason.tlu_connect.utils.AppConst;
import teneocto.thiemjason.tlu_connect.utils.Utils;

public class RegisterSocialNetwork extends AppCompatActivity {
    public static String TAG = "==> Register Social Network";

    FloatingActionButton mFloatingButton;
    BottomSheetFragment mBottomSheetFragment;

    // Main recycle view
    RecyclerView mRecyclerView;
    RegisterAdapter mRegisterAdapter;
    View mEmpty;

    // Buttons
    Button mBackButton;
    Button mNextButton;

    // View model
    RegisterSocialNetworkViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_social_network);
        viewModel = ViewModelProviders.of(this).get(RegisterSocialNetworkViewModel.class);
        viewModel.sharedDTOArrays = new ArrayList<>();
        this.initRecycleView();

        this.mFloatingButton = findViewById(R.id.btn_register_social_add);
        this.mFloatingButton.setOnClickListener(v -> fabOnClick());

        mBackButton = findViewById(R.id.btn_register_social_back);
        mNextButton = findViewById(R.id.btn_register_social_facebook_next);
        mEmpty = findViewById(R.id.register_social_network_empty);
        mEmpty.setVisibility(View.GONE);

        mBackButton.setOnClickListener(v -> backButton());
        mNextButton.setOnClickListener(v -> nextButton());
    }


    /**
     * Init recycle view
     */
    private void initRecycleView() {
        this.mRecyclerView = findViewById(R.id.register_social_recycle_view);
        this.mRegisterAdapter = new RegisterAdapter(this, viewModel.sharedDTOArrays);
        this.mRecyclerView.setAdapter(this.mRegisterAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRegisterAdapter.setOnEditTextChange(new RegisterAdapter.OnEditTextChange() {
            @Override
            public void beforeTextChanged(int position, String text) {

            }

            @Override
            public void onTextChanged(int position, String text) {

            }

            @Override
            public void afterTextChanged(int position, String text) {
                Log.i(AppConst.TAG_RegisterSocialNetworkViewModel, viewModel.sharedDTOArrays.get(position).getUrl() + " ==> text: " + text);
                viewModel.sharedDTOArrays.get(position).setUrl(text);
            }
        });
        mRegisterAdapter.setOnItemClickListener((view, position) -> {
            viewModel.sharedDTOArrays.remove(position);
            Log.i(AppConst.TAG_RegisterSocialNetworkViewModel, " tesssttt: position "+position);
            Log.i(AppConst.TAG_RegisterSocialNetworkViewModel, " tesssttt: viewmodel"+viewModel.sharedDTOArrays.size());
            Log.i(AppConst.TAG_RegisterSocialNetworkViewModel, " tesssttt: adapter "+mRegisterAdapter.sharedDTOArrays.size());

            mRegisterAdapter.notifyItemRemoved(position);
            mRegisterAdapter.notifyItemRangeChanged(position, viewModel.sharedDTOArrays.size());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(String name) {
        SharedDTO sharedDTO = null;

        if ("Instagram".equals(name)) {
            sharedDTO = new SharedDTO(Utils.getRandomUUID(), AppConst.SP_CURRENT_USER_ID, Utils.getSocialNWDTOFromName("Instagram").getId(), "https://instagram.com/");
        }

        if ("Twitter".equals(name)) {
            sharedDTO = new SharedDTO(Utils.getRandomUUID(), AppConst.SP_CURRENT_USER_ID, Utils.getSocialNWDTOFromName("Twitter").getId(), "https://twitter.com/");
        }

        if ("Snapchat".equals(name)) {
            sharedDTO = new SharedDTO(Utils.getRandomUUID(), AppConst.SP_CURRENT_USER_ID, Utils.getSocialNWDTOFromName("Snapchat").getId(), "https://snapchat.com/add/");
        }

        if ("LinkedIn".equals(name)) {
            sharedDTO = new SharedDTO(Utils.getRandomUUID(), AppConst.SP_CURRENT_USER_ID, Utils.getSocialNWDTOFromName("LinkedIn").getId(), "https://linkedin.com/in/");
        }

        if ("Facebook".equals(name)) {
            sharedDTO = new SharedDTO(Utils.getRandomUUID(), AppConst.SP_CURRENT_USER_ID, Utils.getSocialNWDTOFromName("Facebook").getId(), "https://facebook.com/");
        }
        viewModel.addShared(sharedDTO);
        this.mRegisterAdapter.notifyItemInserted(viewModel.sharedDTOArrays.size());
    }

    /**
     * Handler when user clicked on Floating Action button
     */

    private void fabOnClick() {
        this.mBottomSheetFragment = new BottomSheetFragment(this, new BottomSheetFragment.OnItemClick() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(View view, int position) {
                bottomSheetItemClick(view, position);
            }
        });
        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
    }

    /**
     * On UIBottomSheetItemDTO click
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void bottomSheetItemClick(View view, int position) {
        TextView name = view.findViewById(R.id.action_item_name);
        this.mBottomSheetFragment.dismiss();
        addItem(name.getText().toString());
    }

    void backButton() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void nextButton() {
        if (!viewModel.verifyUserInput()) {
            Toast.makeText(this, "Make sure your " + viewModel.errorField + " URL is correct !", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Register successfully !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Drawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}