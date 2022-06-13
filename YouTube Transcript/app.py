from flask import Flask, request
from transformers import T5ForConditionalGeneration, T5Tokenizer
import datetime
import transcript

app = Flask(__name__)

@app.route('/')
def index_page():
    return "Hello world"

@app.route('/time', methods=['GET'])
def get_time():
    return str(datetime.datetime.now())

@app.route('/transcript', methods=['GET'])
def get_transcript():
    args = request.args
    youtube_id = args.get("youtube_url")
    article = transcript.transcribe(youtube_id)
    return article

if __name__ == '__main__':
    app.run()