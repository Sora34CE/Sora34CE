from youtube_transcript_api import YouTubeTranscriptApi

#transcript_list = YouTubeTranscriptApi.list_transcripts('video_id')
def transcribe(youtube_id):
    # youtube_id = 'pgmOfLyFZZk'
    transcript_list = YouTubeTranscriptApi.list_transcripts(youtube_id)
    text = ""
    for transcript in transcript_list:
        trans_dict_list = transcript.translate('en').fetch()
        for t in trans_dict_list:
            text += t['text']
    return text
# print(transcribe('pgmOfLyFZZk'))
# transcript = transcript_list.find_transcript(['de', 'en'])  
# transcript = transcript_list.find_manually_created_transcript(['de', 'en'])  
# transcript = transcript_list.find_generated_transcript(['de', 'en'])