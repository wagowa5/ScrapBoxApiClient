import requests

"""
Usage:
クライアント生成: ScrapboxAPI({projectname}, [{csrf_token}])

・get_page_info: ページ内容(json)を取得
・get_page_text: ページのraw_textを取得
"""
class ScrapboxAPI:
    def __init__(self, projectname, csrf_token=None):
        self.base_url = "https://scrapbox.io/api/"
        self.projectname = projectname
        self.csrf_token = csrf_token
        self.headers = {}

        # CSRF tokenが指定された場合のヘッダー設定
        if self.csrf_token:
            self.headers["x-csrf-token"] = self.csrf_token

    def _make_request(self, endpoint, params=None):
        """
        ScrapBoxAPIをコールする

        @return [responseContent, isJson]
        json形式のとき[json, True]
        json形式でないとき[text, False]
        """
        url = f"{self.base_url}{endpoint}"
        response = requests.get(url, headers=self.headers, params=params)

        if response.status_code == 200:
            try:
              return [response.json(), True]
            except:
              # jsonに変換できない場合はそのまま返す
              return [response.text, False]
        else:
            response.raise_for_status()

    def get_page_info(self, pagetitle):
        return self._make_request(f"pages/{self.projectname}/{pagetitle}")

    def get_page_text(self, pagetitle):
        return self._make_request(f"pages/{self.projectname}/{pagetitle}/text")

    # ... 他のAPIエンドポイントに対応するメソッドを追加 ...

    def full_text_search(self, query):
        return self._make_request(f"pages/{self.projectname}/search/{query}")

    def get_project_info(self):
        return self._make_request(f"projects/{self.projectname}")

    # ... 更に他のAPIエンドポイントに対応するメソッドを追加 ...

    def get_user_info(self):
        return self._make_request("users/me")

    # CSRF tokenが必要なエンドポイントのメソッドも同様に追加 ...


