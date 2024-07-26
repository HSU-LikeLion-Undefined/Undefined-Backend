package com.likelion.RePlay.global.amazon.S3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;


@Service
public class S3Service {

    // AWS S3와 상호작용하는 클라이언트 객체
    private final S3Client s3Client;
    private final String bucketName;
    private final Region region; // 변경된 부분

    // 생성자를 통해 S3 객체 초기화
    public S3Service(@Value("${cloud.aws.credentials.accessKey}") String accessKey,
                     @Value("${cloud.aws.credentials.secretKey}") String secretKey,
                     @Value("${cloud.aws.region.static}") String region,
                     @Value("${cloud.aws.s3.bucket}") String bucketName) {
        // builder를 사용하여 S3 클라이언트 생성
        this.s3Client = S3Client.builder()
                .region(Region.of(region)) // 주입받은 region 설정
                // 주입받은 AWS 자격 증명을 설정
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        this.bucketName = bucketName;
        this.region = Region.of(region);
    }

    // S3에 파일을 업로드하고, 업로드 된 파일의 url 반환
    // MultipartFile file -> 업로드할 파일을 인자로 받음
    public String uploadFile(MultipartFile file) throws IOException {
        // S3에 저장할 때 사용할 고유한 키를 생성 (시간 + 원본 파읾영 조합 )
        String key = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // PutObjectRequest putObjectRequest : S3에 파일을 업로드하기 위한 요청 객체
        // 버킷 이름과 파일 키 설정
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // PutObjectRequest와 파일 데이터를 사용하여 S3에 파일을 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // 업로드된 파일의 url 반환
        return getPublicUrl(key);
    }

    // getPublicUrl : 파일 키를 받아 S3 버킷에 업로드된 파일의 공개 URL을 생성
    private String getPublicUrl(String key) {
        // String.format -> url을 형식화하여 반환
        // url : S3 버킷과 리전 정보를 포함하여 파일에 접근할 수 있는 주소
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), key); // 변경된 부분
    }
}

/*

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

	private final AmazonS3 amazonS3;
	private final AmazonConfig amazonConfig;

	public String uploadFile(MultipartFile file){
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		String keyName = generateUuid();

		try {
			PutObjectResult putObjectResult = amazonS3.putObject(
					new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
			log.info("result={}", putObjectResult.getContentMd5());
		}catch (IOException e){
			log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
		}


		return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
	}

	private String generateUuid(){
		return UUID.randomUUID().toString();
	}
}

* */