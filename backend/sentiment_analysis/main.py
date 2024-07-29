from transformers import pipeline
from fastapi import FastAPI, APIRouter, Request

app = FastAPI()
router = APIRouter()

distilled_student_sentiment_classifier = pipeline(
    model="lxyuan/distilbert-base-multilingual-cased-sentiments-student",
    top_k=1
)

@router.post("/sentiment")
async def classify_sentiment(request: Request):
    data = await request.json()
    input_text = data.get("input")
    if input_text:
        result = distilled_student_sentiment_classifier(input_text)
        if result and isinstance(result, list) and len(result) > 0 and isinstance(result[0], list) and len(result[0]) > 0:
            label = result[0][0].get("label")
            return {"label": label}
    return {"error": "Invalid input"}


app.include_router(router)


if __name__ == "__main__":
    import uvicorn

    # run uvicorn with reload
    uvicorn.run("main:app", port=8000, reload=True)
