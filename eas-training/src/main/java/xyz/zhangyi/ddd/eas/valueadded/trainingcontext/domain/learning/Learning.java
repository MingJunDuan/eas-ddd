package xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.learning;

import xyz.zhangyi.ddd.core.stereotype.Aggregate;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.course.CourseId;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.training.TrainingId;

@Aggregate
public class Learning {
    private String learningId;
    private CourseId courseId;
    private TrainingId trainingId;
    private String traineeId;

    public Learning(String learningId, CourseId courseId, TrainingId trainingId, String traineeId) {
        this.learningId = learningId;
        this.courseId = courseId;
        this.traineeId = traineeId;
        this.traineeId = traineeId;
    }
}