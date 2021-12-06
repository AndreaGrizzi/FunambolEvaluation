package com.funambol.funeval;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.funambol.funeval.databinding.ActivityFullscreenBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String baseUrl = "https://picsum.photos/", imgUrl = "1024";

    private ArrayList<String> viewTags;
    private AlertDialog newGameDialog = null, exitDialog = null;
    private final HashMap<String, IndexedView> viewsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.funambol.funeval.databinding.ActivityFullscreenBinding binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IndexedView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;

        viewTags = new ArrayList<>(Arrays.asList("a1", "a2", "a3", "b1", "b2", "b3", "c1", "c2", "c3"));


        this.getOnBackPressedDispatcher().addCallback(this, getOnBackCallback());


        imageView1 = binding.img1;
        imageView2 = binding.img2;
        imageView3 = binding.img3;
        imageView4 = binding.img4;
        imageView5 = binding.img5;
        imageView6 = binding.img6;
        imageView7 = binding.img7;
        imageView8 = binding.img8;
        imageView9 = binding.img9;

        imageView1.setTag(viewTags.get(0));
        imageView2.setTag(viewTags.get(1));
        imageView3.setTag(viewTags.get(2));
        imageView4.setTag(viewTags.get(3));
        imageView5.setTag(viewTags.get(4));
        imageView6.setTag(viewTags.get(5));
        imageView7.setTag(viewTags.get(6));
        imageView8.setTag(viewTags.get(7));
        imageView9.setTag(viewTags.get(8));

        imageView1.setTarget(1);
        imageView2.setTarget(2);
        imageView3.setTarget(3);
        imageView4.setTarget(4);
        imageView5.setTarget(5);
        imageView6.setTarget(6);
        imageView7.setTarget(7);
        imageView8.setTarget(8);
        imageView9.setTarget(9);

        imageView1.setOnLongClickListener(getLongTouchListener());
        imageView2.setOnLongClickListener(getLongTouchListener());
        imageView3.setOnLongClickListener(getLongTouchListener());
        imageView4.setOnLongClickListener(getLongTouchListener());
        imageView5.setOnLongClickListener(getLongTouchListener());
        imageView6.setOnLongClickListener(getLongTouchListener());
        imageView7.setOnLongClickListener(getLongTouchListener());
        imageView8.setOnLongClickListener(getLongTouchListener());
        imageView9.setOnLongClickListener(getLongTouchListener());

        imageView1.setOnDragListener(getOnDragListener());
        imageView2.setOnDragListener(getOnDragListener());
        imageView3.setOnDragListener(getOnDragListener());
        imageView4.setOnDragListener(getOnDragListener());
        imageView5.setOnDragListener(getOnDragListener());
        imageView6.setOnDragListener(getOnDragListener());
        imageView7.setOnDragListener(getOnDragListener());
        imageView8.setOnDragListener(getOnDragListener());
        imageView9.setOnDragListener(getOnDragListener());

        viewsMap.put(imageView1.getTag().toString(), imageView1);
        viewsMap.put(imageView2.getTag().toString(), imageView2);
        viewsMap.put(imageView3.getTag().toString(), imageView3);
        viewsMap.put(imageView4.getTag().toString(), imageView4);
        viewsMap.put(imageView5.getTag().toString(), imageView5);
        viewsMap.put(imageView6.getTag().toString(), imageView6);
        viewsMap.put(imageView7.getTag().toString(), imageView7);
        viewsMap.put(imageView8.getTag().toString(), imageView8);
        viewsMap.put(imageView9.getTag().toString(), imageView9);

        getRemoteImage(imgUrl);

        createNewGameDialog();
        createExitDialog();

    }

    private OnBackPressedCallback getOnBackCallback() {

        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                exitDialog.show();
            }
        };
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void getRemoteImage(String strUrl) {

        Retrofit retroAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(BitmapConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ImageService service = retroAdapter.create(ImageService.class);
        Observable<Bitmap> bitmapObservable = service.getBitmap(strUrl);
        bitmapObservable.subscribeOn(Schedulers.newThread())
                .onErrorReturnItem(getLocalImage())
                .doOnError(Throwable::printStackTrace)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadImages);

    }

    //load images

    private Bitmap getLocalImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeResource(getResources(), R.drawable.img1, options);
    }

    private void loadImages(Bitmap image) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int displaySide = Math.min(displayMetrics.heightPixels, displayMetrics.widthPixels);
        int imgSide = Math.floorDiv(displaySide, 3);
        List<Bitmap> dList = new ArrayList<>();

        if (image == null) {
            image = Bitmap.createScaledBitmap(getLocalImage(), displaySide, displaySide, true);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        dList.add(Bitmap.createBitmap(image, 0, 0, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, imgSide, 0, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, Math.multiplyExact(imgSide, 2), 0, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, 0, imgSide, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, imgSide, imgSide, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, Math.multiplyExact(imgSide, 2), imgSide, imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, 0, Math.multiplyExact(imgSide, 2), imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, imgSide, Math.multiplyExact(imgSide, 2), imgSide, imgSide));
        dList.add(Bitmap.createBitmap(image, Math.multiplyExact(imgSide, 2), Math.multiplyExact(imgSide, 2), imgSide, imgSide));

        for (String key : viewsMap.keySet()) {
            Objects.requireNonNull(viewsMap.get(key)).setIndexDraw(dList);
        }
        shuffler();
    }

    private void shuffler() {
        ArrayList<Integer> random = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(random);
        for (int i = 0; i < viewTags.size(); i++) {
            Objects.requireNonNull(viewsMap.get(viewTags.get(i))).setIndex(random.get(i));
        }


    }

    //drag

    private View.OnLongClickListener getLongTouchListener() {
        return v -> {
            if (Objects.requireNonNull(viewsMap.get(v.getTag())).isLocked()) {
                //right position. no more moves
                return false;
            }

            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item);
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDragAndDrop(dragData,
                    shadow,
                    null,
                    0

            );
            return true;
        };
    }

    private View.OnDragListener getOnDragListener() {
        return (view, dragEvent) -> {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                case DragEvent.ACTION_DRAG_ENDED:
                case DragEvent.ACTION_DRAG_ENTERED:
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
                case DragEvent.ACTION_DROP:
                    ClipData.Item clipItem = dragEvent.getClipData().getItemAt(0);
                    String starterTag = clipItem.getText().toString();
                    IndexedView starter = viewsMap.get(starterTag);
                    assert starter != null;
                    if (starter.isLocked()||((IndexedView) view).isLocked()) return false;
                    starter.swapIndex(((IndexedView) view).swapIndex(starter.getIndex()));
                    if (areYouWinningSon()) {
                        System.out.println("-------------------Victory------------------------");
                        newGameDialog.show();
                    }
                    break;
            }
            return true;
        };
    }

    private boolean areYouWinningSon() {
        for (int i = 0; i < 9; i++) {
            IndexedView indexedView = viewsMap.get(viewTags.get(i));
            assert indexedView != null;
            if (!indexedView.isLocked()) {
                return false;
            }
        }
        return true;
    }

    private void createNewGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.victory);
        builder.setPositiveButton(R.string.reload, (dialogInterface, i) -> {
            assert newGameDialog != null;
            newGameDialog.dismiss();
            MainActivity.this.getRemoteImage(imgUrl);
        });
        builder.setNegativeButton(R.string.exit, (dialogInterface, i) -> {
            newGameDialog.dismiss();
            System.exit(-1);
        });
        newGameDialog = builder.create();
    }

    private void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.q_exit);
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            assert exitDialog != null;
            exitDialog.dismiss();
            System.exit(-1);
        });
        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> exitDialog.dismiss());
        exitDialog = builder.create();
    }

}