package cl.isosalud.service.service.publicweb.otp;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OtpServiceImpl implements OtpService {

    private static final Integer EXPIRE_MINUTES = 30;
    private static final LoadingCache<String, Integer> otpCache;

    static {
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return null;
                    }
                });
    }

    @Override
    public int generateOtp(String key) {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000);
        otpCache.put(key, otp);
        return otp;
    }

    @Override
    public Optional<Integer> getOtp(String key) {
        try {
            return Optional.ofNullable(otpCache.get(key));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean invalidateOtp(String key) {
        try {
            otpCache.invalidate(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
