package hello.aop.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository repository;

    public void reqeust(String itemId) {
        repository.save(itemId);
    }
}
