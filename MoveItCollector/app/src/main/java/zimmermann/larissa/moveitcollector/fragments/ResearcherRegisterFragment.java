package zimmermann.larissa.moveitcollector.fragments;


import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import zimmermann.larissa.moveitcollector.MainActivity;
import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.SplashActivity;
import zimmermann.larissa.moveitcollector.database.Researcher;
import zimmermann.larissa.moveitcollector.repository.LoginActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.DataUtils;
import zimmermann.larissa.moveitcollector.utils.Gender;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResearcherRegisterFragment extends Fragment {

    private static final String TAG = ResearcherRegisterFragment.class.getName();
    private static final int RADIO_BUTTON_UNSELECTED = -1;

    private LoginActivityViewModel mLoginActivityViewModel;
    private LiveData<List<Researcher>> mResearcherList;
    private Researcher mResearcher;

    @BindView(R.id.ivProfilePhoto)
    RoundedImageView ivProfilePhoto;
    @BindView(R.id.etAcademicInstitute)
    EditText etAcademicInstitute;
    @BindView(R.id.etResearcherBirthday)
    EditText etResearcherBirthday;
    @BindView(R.id.rgGender)
    RadioGroup rgGender;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.btAddDate)
    Button btAddDate;
    @BindView(R.id.btAdditionalInfo)
    Button btAdditionalInfo;

    private Unbinder unbinder;
    private int mDay;
    private int mMonth;
    private int mYear;
    private String mBirthday = null;

    public ResearcherRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_researcher_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        mLoginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginActivityViewModel.class);
        mLoginActivityViewModel.getReseacherList().observe(getActivity(), new Observer<List<Researcher>>() {
            @Override
            public void onChanged(@Nullable List<Researcher> researchers) {
                mResearcher = researchers.size() > 0 ? researchers.get(0) : null;
                configurePersonAvatar();
            }
        });

        mResearcherList = mLoginActivityViewModel.getReseacherList();
        if(mResearcherList != null && mResearcherList.getValue() != null) {
            mResearcher = mResearcherList.getValue().get(0);
            configurePersonAvatar();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btAddDate)
    public void addDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mBirthday = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        etResearcherBirthday.setText(mBirthday);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.btAdditionalInfo)
    public void saveAdditionalInfo() {
        if(TextUtils.isEmpty(etAcademicInstitute.getText())) {
            etAcademicInstitute.setError(getString(R.string.required_field));
        }
        else if(TextUtils.isEmpty(etResearcherBirthday.getText())) {
            etResearcherBirthday.setError(getString(R.string.required_field));
        }
        else if(rgGender.getCheckedRadioButtonId() == RADIO_BUTTON_UNSELECTED) {
            rbFemale.setError(getString(R.string.select_at_least_one));
            rbMale.setError(getString(R.string.select_at_least_one));
        }
        else {
            //Save into mResearcher and update
            String academicInstitute = etAcademicInstitute.getText().toString();
            String birthday = etResearcherBirthday.getText().toString();
            Gender gender = rgGender.getCheckedRadioButtonId() == rbFemale.getId() ? Gender.F : Gender.M;

            java.util.Date birthdayDate = DataUtils.convertStringToDate(birthday);
            Researcher researcher = mResearcher;
            researcher.setAcademicInstitute(academicInstitute);
            researcher.setBirthday(birthdayDate);
            researcher.setGender(gender);

            mLoginActivityViewModel.update(researcher);

            //Call Next Activity
            ActivityOptionsCompat activityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            ivProfilePhoto,
                            ivProfilePhoto.getTransitionName());

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra(MainActivity.RESEARCHER_PROFILE_URL, mResearcher.getPhotoUrl());
            getActivity().startActivity(intent, activityOptionsCompat.toBundle());

            //Kill this activity
            getActivity().finish();
        }
    }

    @OnClick({R.id.rbFemale, R.id.rbMale})
    public void selectGender() {
        rbFemale.setError(null);
        rbMale.setError(null);
    }

    private void configurePersonAvatar() {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.get().load(mResearcher.getPhotoUrl())
                .fit()
                .transform(transformation)
                .placeholder(getContext().getDrawable(R.drawable.ic_person_accent_48dp))
                .into(ivProfilePhoto);
    }
}
