# ▶️ ssh를 활용하여 특정 server로 배포하기
<br/>

ssh를 사용하여 서버를 빌드용 배포용으로 나눠 보자!

<br/>
☕ 목차

<br/>

1. 최신 깃허브 레포를 받은 로컬 젠킨스에서 빌드 후 aws우분투 인스턴스에 배포하기

2. 두개의 aws 인스턴스를 빌드 배포용으로 구분해서 사용한다.
   하나는 젠킨스를 구동하고 나머지 하나는 배포용 이다.
   깃허브 레포에서 수정된 코드는 지속적으로 통합하는건 1번과 같다.

<br/>

☕ 사용 툴
virtual box, mobaxterm, docker, Jenkins, aws, ngrok

<br/>

-------------

### 1. ssh란? 

> `네트워크 상의 다른 컴퓨터에` 로그인 하거나 원격 시스템에서 명령을 실행하고 다른 시스템으로 파일을 복사할 수 있도록 해주는 `응용 프로그램 또는 프로토콜`

<br/>

### 2. 최신 깃허브 레포를 받은 로컬 젠킨스에서 빌드 후 aws우분투 인스턴스에 배포하기

<br/>

#### 1. virtual box - 도커위에 젠킨스 로컬로 띄우기 및 gradle변경 및 ssh 플러그인 설치 

<br/>

✔️ [젠킨스 띄우는 코드는 ci레포 참고](https://github.com/ugyeong0u0/Fisa_CI)

<br/>

<details>
 <summary>gradle변경 및 ssh 플러그인 설치</summary>
 <div markdown="1">
   
![gradlealter](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/fd1d988d-c14c-4679-a4c2-a94800e4c981)
   
##### 🥹주의 ssh 설치가 다 완료됐다고하면 다른 페이지로 넘어가기!! 중간에 넘어가면 설치가된것도 안된것도 아닌 상태 지워도 안되고 몇분간 사용 못함...

![sshplugin](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/d33489bb-355e-40dc-a1b4-c234ebf7ce80)

 </div>
 </details>

#### 2. publish over ssh에 파일을 전달 받을 aws 우분투의 .pem키 넣기

##### 2-1. aws의 키페어 ppk만 보유하고 있을 경우 tool을 사용한 pem key 생성

 <details>
 <summary>aws 키페어 </summary>
 <div markdown="1">
 인스턴스 생성시에 .ppk로 생성한것 

![Untitled](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/15884ca7-7eee-4b61-b944-8655b5d7ac11)

 </div>
 </details>

##### 2-2. .ppk로 .pem 만들기 
1단계 - MobaXterm의 Tool 선택
2단계 - Tool의 MobaKeyGen (SSH key generator) 선택
3단계 - “Load” 버튼을 통한 ppk 파일 선택
4단계 - "Conversions" Export open SSH key(2번째 메뉴) 누르기 
5단계 - .pem으로 저장 

총순서

![converpem](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/88fd6406-b10d-45f6-8350-cefd15fbae75)

##### 2-2. genkins에 pem등록( '전달 받을 ' aws 우분투의 .pem, 전달하는 애꺼 아님!!)

![genkins에pem등록](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/168f0d45-211f-45e9-bfa1-fc93a37fe727)

![ssh전체](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/77c17518-1bc5-4b50-ae3c-8d52553b7b7f)


### 3. item 생성 및 깃웹훅 젠킨스에 설정 

![item생성](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/d262c8fd-95dc-4cce-96e3-6baf00b0d2cc)

### 4. item의 configuration 설정

 ![pipeline](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/c1470614-5e04-4b55-9cbb-f3e68a1b1e92)

### 5. pipeline syntext 설정

![publicssh](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/a04f645d-dee4-41d2-afcd-da8e5d17385b)

![publicssh2](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/16c60697-778e-470e-9d8b-b5ad28b28562)

![publicssh3](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/2c84da2b-a459-4920-962c-9f5813e11738)

![publicssh4](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/53d643dd-e8ba-418a-838c-053531418292)

![publicssh5](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/0d51b6e2-a155-475b-841e-b166de506474)

![publicssh](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/a04f645d-dee4-41d2-afcd-da8e5d17385b)


🥹이제 빨간 2번을 만들어보자 바로 위위 사진에서 복사한 값을 들여쓰기 맞춰줘야한다.(여기서 계속 오류났음 ㅠㅠ)

양식

```

pipeline {
    agent any
    stages {      
        stage('git clone') {
            steps {
                
                echo 'start ********'
                
               git branch: 'main', url: 'https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu.git' 
                
                echo '************* end'
            }
        }     
    
        stage('list view') {
            steps {
                echo '리스트 보기'
                sh ''' ls -al '''
            }
        }
        
        stage('build') {
            steps {
                echo 'Building Gradle project'
                sh ''' ./gradlew build '''
            }
        }
        stage('deploy') {
            steps {
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'ssh 이름(위에서 설정한 ssh server name, 나의 경우 myec2micro )', transfers: [
                                sshTransfer(
                                    cleanRemote: false, 
                                    excludes: '', 
                                    execCommand: ' echo "push success" ', 
                                    execTimeout: 120000, 
                                    flatten: false, 
                                    makeEmptyDirs: false, 
                                    noDefaultExcludes: false, 
                                    patternSeparator: '[, ]+', 
                                    remoteDirectory: '/test', 
                                    remoteDirectorySDF: false, 
                                    removePrefix: 'build/libs', 
                                    sourceFiles: 'build/libs/*SNAPSHOT.jar'
                                )
                            ], 
                            usePromotionTimestamp: false, 
                            useWorkspaceInPromotion: false, 
                            verbose: true
                        )
                    ]
                )
            }
        }
    }
}

````
<br/>

### 6. 깃허브 해당 레포 settings에서 웹 훅 설정

이때 젠킨스를 로컬에서 돌리기 때문에 ngrok 사용 -> ✔️ [ngrok관련은 ci레포 참고](https://github.com/ugyeong0u0/Fisa_CI)

<br/>

### 7. ngrok 대신 다른 aws인스턴스 사용시 
1~5까지 방법은 같으며 젠킨스 접속 url과 웹훅 payload에 http://젠킨스가 설치된 퍼블릭 ipv4 주소 혹은 퍼블릭 ipv dns 사용 혹은 aws 데시보드에서 보안접속 젠킨스 포트 추가 해주면 http://localhost:포트번호 로도 접속가능  

☕⭐😇 중간에 네트워크 에러로 인스턴스와 mobaxterm 연결이 끊어졌었다. 이때 바로 재접속을 했자만 전에 만든 test디렉터리를 찾을 수 없없다. 그 이유는 aws 인스턴스를 종료하지 않는한 데이터는 살아있는데 ip가 변경되었기 때문이였다. elastic ip로 ip고정사용해주면 되는데 돈이 나간다고 한다. 

### 8. 결과

![결과](https://github.com/ugyeong0u0/localJekins_to_ec2Ubuntu/assets/120684605/bdcb3e30-ccac-4f1f-8cc2-71b6acccdb1a)

