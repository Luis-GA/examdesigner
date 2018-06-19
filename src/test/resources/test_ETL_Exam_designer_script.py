import unittest
from unittest.mock import Mock, patch
from unittest.mock import MagicMock

from ETL_Exam_designer_script import  ETL

def Mocktext():
    """
    Mock for response
    :return:
    """
    Mresponse = '''{"Key": "value"}'''
    return Mresponse


class mock_open():
    """
    Mock for open
    """

    code = None

    def __init__(self, code=None):
        """
        Mock for __init__
        :param code:
        """
        self.code = code
        self.mockResponse = MagicMock()

    def __enter__(self):
        """
        Mock for __enter__
        :return:
        """
        self.mockResponse.status = self.code
        self.mockResponse.text = Mock(side_effect=Mocktext)
        return self.mockResponse

    def __exit__(self, exc_type, exc_val, exc_tb):
        """
        Mock for __exit__
        :param exc_type:
        :param exc_val:
        :param exc_tb:
        :return:
        """
        pass

class test_prueba(unittest.TestCase):


    @patch('builtins.open', return_value=mock_open())
    @patch("json.dumps", MagicMock(return_value='''{
    "idTopic": 1,
    "questions": [
        {
            "bodyObjects": [],
            "choices": {
                "a": {
                    "bodyObjects": [],
                    "title": "La primera es m\u00e1s amplia que la segunda"
                },
                "b": {
                    "bodyObjects": [],
                    "title": "La segunda es m\u00e1s amplia que la primera"
                },
                "c": {
                    "bodyObjects": [],
                    "title": "La primera es un proceso y la segunda no"
                },
                "d": {
                    "bodyObjects": [],
                    "title": "No hay diferencias entre ellas"
                }
            },
            "correctChoices": [
                "La segunda es m\u00e1s amplia que la primera"
            ],
            "duration": 0,
            "title": "La diferencia entre seguridad inform\u00e1tica y seguridad de la informaci\u00f3n fundamentalmente radica en que:",
            "type": "TEST",
            "weight": 0
        }
    ],
    "topic": "CONCEPTOS DE TEOR\u00cdA DE LA INFORMACI\u00d3N"
}'''))
    def test_sucessfull_ETL(self,mockopen):
        mockopen.return_value.mockResponse.readlines.return_value=["1. La diferencia entre seguridad informática y seguridad de la información fundamentalmente radica en que:",
"a.	La primera es más amplia que la segunda",
"b.	La segunda es más amplia que la primera (CORRECTA)",
"c.	La primera es un proceso y la segunda no",
"d.	No hay diferencias entre ellas"
]
        ETL()
        self.assertTrue(mockopen.return_value.mockResponse.readlines.called)

    @patch('builtins.open', return_value=mock_open())
    def test_failed_ETL(self,mockopen):
        mockopen.return_value.mockResponse.readlines.return_value=["1. La diferencia entre seguridad informática y seguridad de la información fundamentalmente radica en que:",
"a.	La primera es más amplia que la segunda",
"b.	La segunda es más amplia que la primera (CORRECTA)",
"c.	La primera es un proceso y la segunda no"
]
        failed=False
        try:
            ETL()
        except Exception as e:
            failed=True
        self.assertTrue(failed)
