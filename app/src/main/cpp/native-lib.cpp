#include <jni.h>
#include <string>
#include "FileManager.h"
#include <memory>

#define JNIEX(y) extern "C" JNIEXPORT void JNICALL Java_com_example_exptrcandroid_MainActivity_##y
std::unique_ptr<FileManager> fm;

//extern "C" JNIEXPORT jstring JNICALL
//Java_com_example_exptrcandroid_MainActivity_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}

JNIEX(InitFileManager)(
        JNIEnv* env,
        jobject /*this*/)
{
    fm = std::make_unique<FileManager>("\\\\192.168.178.45\\share\\ExpTrc\\OneTimeExpenses.exptrc", "\\\\192.168.178.45\\share\\ExpTrc\\MonthlyExpenses.exptrc", "\\\\192.168.178.45\\share\\ExpTrc\\OneTimeTakings.exptrc", "\\\\192.168.178.45\\share\\ExpTrc\\MonthlyTakings.exptrc", "\\\\192.168.178.45\\share\\ExpTrc\\General.exptrc");
}


JNIEX(ModifyGeneralData)(
        JNIEnv* env,
        jobject/*this*/)
{
    GeneralData gd = fm->GetGeneralData();
    gd.balance = 0.0;
    fm->WriteGeneral(gd);
}
